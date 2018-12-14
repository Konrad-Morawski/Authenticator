package com.intive.kmorawski.authenticator;

import java.util.Arrays;
import java.util.Collection;

/**
 * We pretend it's an actual database, presumably cloud-based,
 * and not suitable for testing purposes.
 */
public class UserDatabase implements AccountsProvider {
    @Override
    public Collection<Account> getAccounts() {
        return Arrays.asList(
                new Account("user", "password"),
                new Account("user2", "password2"));
    }
}