package org.instituteatri.deep.service.strategy.implstrategy;

import org.instituteatri.deep.infrastructure.exceptions.user.PasswordsNotMatchException;
import org.instituteatri.deep.service.strategy.interfaces.PasswordValidationStrategy;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidationStrategyImpl implements PasswordValidationStrategy {
    @Override
    public void validate(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordsNotMatchException();
        }
    }
}
