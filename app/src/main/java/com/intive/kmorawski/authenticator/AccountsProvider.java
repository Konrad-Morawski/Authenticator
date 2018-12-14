package com.intive.kmorawski.authenticator;

import java.util.Collection;

/**
 * Provides access to the accounts of registered users (their logins and passwords)
 */
public interface AccountsProvider {
    Collection<Account> getAccounts();
}