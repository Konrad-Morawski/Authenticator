package com.intive.kmorawski.authenticator;

import android.support.annotation.VisibleForTesting;

/**
 * NOTE: for training purposes only.
 * Not good architecture.
 * Preferably use DI instead (Dagger 2 being the most popular standard of choice now).
 *
 * This is effectively a primitive version of the Service Locator.
 * See http://blog.ploeh.dk/2010/02/03/ServiceLocatorisanAnti-Pattern/ for some thoughts.
 *
 * Note the downsides of the Service Locator can be mitigated
 * with a well-designed SL library, and might be acceptable for simple projects
 * where a complex solution such as Dagger is an overkill.
 * Especially in Kotlin, which makes it easier with some syntax sugar - see Koin.
 *
 * Eg. see:
 * https://android.jlelse.eu/moving-from-dagger-to-koin-simplify-your-android-development-e8c61d80cddb
 * https://medium.com/@charbgr/bye-bye-dagger-1494118dcd41
 */
public class AccountsProviderFactory {
    private static AccountsProvider defaultProvider = new UserDatabase();

    public static AccountsProvider getAccountsProvider() {
        return defaultProvider;
    }

    // NOTE: code smell
    @VisibleForTesting
    public static void replaceAccountsProvider(AccountsProvider nonStandardAccountsProvider) {
        defaultProvider = nonStandardAccountsProvider;
    }
}