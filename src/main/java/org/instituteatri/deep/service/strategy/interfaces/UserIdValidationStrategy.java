package org.instituteatri.deep.service.strategy.interfaces;

import org.springframework.security.core.Authentication;

public interface UserIdValidationStrategy {
    void validate(Authentication authentication, String userId);
}
