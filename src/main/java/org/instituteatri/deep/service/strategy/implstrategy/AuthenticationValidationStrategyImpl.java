package org.instituteatri.deep.service.strategy.implstrategy;

import org.instituteatri.deep.infrastructure.exceptions.user.NotAuthenticatedException;
import org.instituteatri.deep.service.strategy.interfaces.AuthenticationValidationStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationValidationStrategyImpl implements AuthenticationValidationStrategy {
    @Override
    public void validate(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }
    }
}
