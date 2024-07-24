package com.example.usermanagmentapp.ui.add

import android.os.IBinder
import android.view.View
import android.view.WindowManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.example.usermanagmentapp.R
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Random


@RunWith(AndroidJUnit4::class)

class AddUserActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(AddUserActivity::class.java)
    private lateinit var decorView: View
    private val device: UiDevice
        get() = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())


    @Test
    fun testAddUserNameFieldEmptyShowsSnackBar() {
        activityScenarioRule.scenario.onActivity {
            decorView = it.window.decorView
        }
        onView(withId(R.id.emailEditText)).perform(typeText("test@mail.com"))
        onView(withId(R.id.maleButton)).perform(click())
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.submitButton)).perform(click())

        onView(withId(R.id.nameEditText)).check(matches(hasErrorText("Enter A name")));
    }

    @Test
    fun testAddUserEmailFieldEmptyShowsSnackBar() {
        activityScenarioRule.scenario.onActivity {
            decorView = it.window.decorView
        }
        onView(withId(R.id.nameEditText)).perform(typeText("Test User"))
        onView(withId(R.id.maleButton)).perform(click())
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.submitButton)).perform(click())

        onView(withId(R.id.emailEditText)).check(matches(hasErrorText("Invalid email address")));
    }

    @Test
    fun testAddUserWithMissingGenderShowsSnackBar() {
        activityScenarioRule.scenario.onActivity {
            decorView = it.window.decorView
        }
        onView(withId(R.id.nameEditText)).perform(typeText("Test User"))
        onView(withId(R.id.emailEditText)).perform(typeText("test@mail.com"))

        Espresso.closeSoftKeyboard()
        onView(withId(R.id.submitButton)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Select a Gender")))
    }

    @Test
    fun testAddUserSuccess() {
        activityScenarioRule.scenario.onActivity {
            decorView = it.window.decorView
        }
        val nextInt = Random().nextInt()
        onView(withId(R.id.nameEditText)).perform(typeText("Test User $nextInt"))
        onView(withId(R.id.emailEditText)).perform(typeText("test$nextInt@mail.com"))
        onView(withId(R.id.maleButton)).perform(click())
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.submitButton)).perform(click())


        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("User added successfully")))
    }

}

