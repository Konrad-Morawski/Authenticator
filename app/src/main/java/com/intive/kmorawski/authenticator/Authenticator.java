package com.intive.kmorawski.authenticator;

import android.text.TextUtils;

public class Authenticator {
    private final AccountsProvider accountsProvider;

    /**
     * @param accountsProvider provides accounts data to verify an authentication attempt against
     */
    public Authenticator(AccountsProvider accountsProvider) {
        this.accountsProvider = accountsProvider;
    }

    public AuthenticationResult authenticate(String login, String password) {
        if (TextUtils.isEmpty(login)) {
            return AuthenticationResult.BLANK_LOGIN;
        }
        if (TextUtils.isEmpty(password)) {
            return AuthenticationResult.BLANK_PASSWORD;
        }

        for (Account account : accountsProvider.getAccounts()) {
            if (account.login.equals(login)) {
                return account.password.equals(password)
                        ? AuthenticationResult.OK
                        : AuthenticationResult.INCORRECT_PASSWORD;
            }
        }

        return AuthenticationResult.NO_USER;
    }
}