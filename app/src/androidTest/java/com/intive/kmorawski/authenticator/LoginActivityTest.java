package com.intive.kmorawski.authenticator;


import android.support.annotation.IdRes;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Collections;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched(); // technically unnecessary, as parent implementation is empty in this case
            AccountsProviderFactory.replaceAccountsProvider(mockedAccountsProvider);
        }
    };


    public AccountsProvider mockedAccountsProvider = new AccountsProvider() {
        @Override
        public Collection<Account> getAccounts() {
            return Collections.singleton(new Account("mocked", "mocked"));
        }
    };

    @Test
    public void givenSomeLoginButNoPassword_whenLogIsTapped_passwordCantBeBlankErrorAppears() {
        enterLogin("whatever");

        tapLogin();

        expectErrorDisplayed("Password can't be blank");
    }

    @Test
    public void givenSomeIncorrectLoginAndPassword_whenLogIsTapped_userNotRegisteredErrorAppears() {
        enterLogin("inexistent user");
        enterPassword("some random password");

        tapLogin();

        expectErrorDisplayed("User is not registered");
    }

    void enterLogin(String login) {
        typeInto(R.id.login_input, login);
    }

    void enterPassword(String password) {
        typeInto(R.id.password_input, password);
    }

    void typeInto(@IdRes int textViewId, String text) {
        onView(withId(textViewId)).perform(typeText(text));
    }

    void tapLogin() {
        onView(withId(R.id.login)).perform(click());
    }

    void expectErrorDisplayed(String expectedErrorMessage) {
        onView(withId(R.id.error_message))
                .check(
                        matches(
                                allOf(
                                        isDisplayed(),
                                        withText(expectedErrorMessage))));
    }
}
