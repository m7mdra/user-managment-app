package com.example.usermanagmentapp.ui.add;

import android.view.View;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.usermanagmentapp.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class AddUserActivityTest {

    @Rule
    public ActivityScenarioRule<AddUserActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AddUserActivity.class);

    @Test
    public void testAddUserNameFieldEmptyShowsSnackBar() {


        onView(withId(R.id.emailEditText)).perform(typeText("test@mail.com"));
        onView(withId(R.id.maleButton)).perform(click());
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submitButton)).perform(click());

        onView(withId(R.id.nameEditText)).check(matches(hasErrorText("Enter A name")));
    }

    @Test
    public void testAddUserEmailFieldEmptyShowsSnackBar() {


        onView(withId(R.id.nameEditText)).perform(typeText("Test User"));
        onView(withId(R.id.maleButton)).perform(click());
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submitButton)).perform(click());

        onView(withId(R.id.emailEditText)).check(matches(hasErrorText("Invalid email address")));
    }

    @Test
    public void testAddUserWithMissingGenderShowsSnackBar() {


        onView(withId(R.id.nameEditText)).perform(typeText("Test User"));
        onView(withId(R.id.emailEditText)).perform(typeText("test@mail.com"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submitButton)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Select a Gender")));
    }

    @Test
    public void testAddUserSuccess() {
 
        int nextInt = new java.util.Random().nextInt();
        onView(withId(R.id.nameEditText)).perform(typeText("Test User " + nextInt));
        onView(withId(R.id.emailEditText)).perform(typeText("test" + nextInt + "@mail.com"));
        onView(withId(R.id.maleButton)).perform(click());
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submitButton)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("User added successfully")));
    }
}
