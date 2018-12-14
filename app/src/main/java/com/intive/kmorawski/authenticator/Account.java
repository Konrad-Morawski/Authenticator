package com.intive.kmorawski.authenticator;

import android.support.annotation.NonNull;

public class Account {
    public final String login;

    // we keep passwords in plaintext
    public final String password;

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        /* note: this is not recommended and only for demonstration purposes.
         * eg. we wouldn't want to risk plaintext password surfacing in application logs, for instance. */
        return String.format("Account{login='%s', password='%s'}", login, password);
    }
}
