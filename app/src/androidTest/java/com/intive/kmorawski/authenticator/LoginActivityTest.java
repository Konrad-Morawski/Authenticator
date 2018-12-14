package com.intive.kmorawski.authenticator;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void givenSomeLoginButNoPassword_whenLogIsTapped_passwordCantBeBlankErrorAppears() {
        onView(withId(R.id.login_input)).perform(typeText("whatever"));
        onView(withId(R.id.login)).perform(click());

        onView(withId(R.id.error_message))
                .check(
                        matches(
                                allOf(
                                        isDisplayed(),
                                        withText("Password can't be blank"))));
    }
}
