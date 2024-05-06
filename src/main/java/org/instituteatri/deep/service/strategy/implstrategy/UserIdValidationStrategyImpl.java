package org.instituteatri.deep.service.strategy.implstrategy;

import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.exception.user.UserAccessDeniedException;
import org.instituteatri.deep.service.strategy.interfaces.UserIdValidationStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserIdValidationStrategyImpl implements UserIdValidationStrategy {
    @Override
    public void validate(Authentication authentication, String userId) {
        String authenticatedUserId = ((User) authentication.getPrincipal()).getId();
        if (!userId.equals(authenticatedUserId)) {
            throw new UserAccessDeniedException();
        }
    }
}
