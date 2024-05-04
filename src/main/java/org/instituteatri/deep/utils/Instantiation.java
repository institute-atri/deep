package org.instituteatri.deep.utils;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.domain.user.User;
import org.instituteatri.deep.domain.user.UserRole;
import org.instituteatri.deep.repositories.TokenRepository;
import org.instituteatri.deep.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class Instantiation implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${user.password}")
    private String userPassword;

    @Override
    public void run(String... args) {
        tokenRepository.deleteAll();

        userRepository.deleteAll();

        String encryptedAdminPassword = passwordEncoder.encode(adminPassword);
        User admin = new User(
                "Rafael",
                "admin@localhost.com",
                encryptedAdminPassword,
                true,
                UserRole.ADMIN);
        userRepository.save(admin);

        String encryptedUserPassword = passwordEncoder.encode(userPassword);
        User user = new User(
                "User",
                "user@localhost.com",
                encryptedUserPassword,
                true,
                UserRole.USER);
        userRepository.save(user);
    }
}
