package com.intive.kmorawski.authenticator;

public enum AuthenticationResult {
    OK,
    NO_USER,
    INCORRECT_PASSWORD,
    BLANK_LOGIN,
    BLANK_PASSWORD;

    boolean isAuthenticated() {
        return this == OK;
    }
}