package com.example.tilecalculatortwo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import com.example.tilecalculatortwo.historypack.HistoryAndAdd;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HistoryAndAddTest {
    @Rule
    public ActivityScenarioRule<HistoryAndAdd> activityRule =
            new ActivityScenarioRule<>(HistoryAndAdd.class);

    @Test
    public void checkVisibility() {
        onView(withId(R.id.BackToMainScreen)).check(matches(isDisplayed()));
        onView(withId(R.id.ShowHistoryButton)).check(matches(isDisplayed()));
        onView(withId(R.id.AddArticleButton)).check(matches(isDisplayed()));
        onView(withId(R.id.ArticleTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.DescriptionTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.BoxVolumeTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.TilesCounterTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.ArticleAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.DescriptionAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.BoxVolumeAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.TilesCounterAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.DownloadButton)).check(matches(isDisplayed()));
        onView(withId(R.id.UploadDataButton)).check(matches(isDisplayed()));
        onView(withId(R.id.ShowHelpButton)).check(matches(isDisplayed()));
    }


    @Test
    public void checkHistoryText() {
        onView(withId(R.id.ShowHistoryButton)).perform(click());
        onView(withId(R.id.HistoryText)).check(matches(isDisplayed()));
    }

    @Test
    public void checkAddingData() {
        onView(withId(R.id.ArticleAdd)).perform(typeText("0"), closeSoftKeyboard());
        onView(withId(R.id.DescriptionAdd)).perform(typeText("name"), closeSoftKeyboard());
        onView(withId(R.id.BoxVolumeAdd)).perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.TilesCounterAdd)).perform(typeText("9"), closeSoftKeyboard());
        onView(withId(R.id.AddArticleButton)).perform(click());
    }

    @Test
    public void checkShowExample() {
        onView(withId(R.id.ShowHelpButton)).perform(click());
        onView(withId(R.id.HistoryText)).check(matches(withText(R.string.ExampleOfFillingText)));
    }
}
