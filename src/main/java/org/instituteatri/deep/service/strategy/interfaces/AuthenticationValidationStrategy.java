package org.instituteatri.deep.service.strategy.interfaces;

import org.springframework.security.core.Authentication;

public interface AuthenticationValidationStrategy {
    void validate(Authentication authentication);
}
