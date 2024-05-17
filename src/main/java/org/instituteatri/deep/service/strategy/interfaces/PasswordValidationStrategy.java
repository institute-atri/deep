package org.instituteatri.deep.service.strategy.interfaces;

public interface PasswordValidationStrategy {
    void validate(String password, String confirmPassword);
}
