package org.instituteatri.deep.service.strategy.interfaces;

public interface EmailAlreadyValidationStrategy {
    void validate(String existingEmail, String newEmail, String userIdToExclude);
}
