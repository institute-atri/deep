package org.instituteatri.deep.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.instituteatri.deep.exception.user.TokenInvalidException;
import org.instituteatri.deep.repository.TokenRepository;
import org.instituteatri.deep.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    final TokenService tokenService;
    final TokenRepository tokenRepository;
    final UserRepository userRepository;
    final HttpServletRequest request;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = recoverTokenFromRequest(request);
            if (token != null) {
                handleAuthentication(token, response);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("[ERROR_FILTER] Error processing security filter: {}", e.getMessage());
            throw e;
        }
    }

    public String recoverTokenFromRequest(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            log.debug("[NO_AUTH_HEADER] No Authorization header found in the request.");
            return null;
        }
        String token = authHeader.replace("Bearer ", "");
        log.debug("[RECOVERED] Token recovered from Authorization header: {}", token);
        return token;
    }

    private void handleAuthentication(String token, HttpServletResponse response)
            throws IOException {

        try {
            String email = tokenService.validateToken(token);
            UserDetails userDetails = getUserDetailsByEmail(email);
            boolean isTokenValid = isTokenValid(token);

            if (userDetails != null && isTokenValid) {
                setAuthenticationInSecurityContext(userDetails);
            } else {
                getError(token);
            }
        } catch (TokenInvalidException e) {
            handleInvalidToken(response, e);
            getError(token);
        }
    }

    private void getError(String token) {
        log.error("[TOKEN_FAILED] User-Agent: {}. IP Address: {}. Validation failed for token: {}",
                getUserAgent(), getIpAddress(), token);
    }

    private String getIpAddress() {
        return request.getRemoteAddr();
    }

    private String getUserAgent() {
        return request.getHeader("User-Agent");
    }

    private UserDetails getUserDetailsByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean isTokenValid(String token) {
        return tokenRepository.findByTokenValue(token)
                .map(t -> !t.isTokenExpired() && !t.isTokenRevoked())
                .orElse(false);
    }

    private void setAuthenticationInSecurityContext(UserDetails userDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleInvalidToken(HttpServletResponse response, TokenInvalidException e)
            throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(e.getMessage());
        log.warn("[TOKEN_INVALID] Invalid token detected: {}", e.getMessage());
    }
}
