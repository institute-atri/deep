package org.instituteatri.deep.infrastructure.exceptions.user;

public class AccountLockedException extends RuntimeException {
    public AccountLockedException() {
        super("Account is locked.");
    }
}
