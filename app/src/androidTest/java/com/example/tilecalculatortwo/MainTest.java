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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void checkVisibility() {
        onView(withId(R.id.SearchingArticle)).check(matches(isDisplayed()));
        onView(withId(R.id.SearchFromTreeMap)).check(matches(isDisplayed()));
        onView(withId(R.id.SearchingText)).check(matches(isDisplayed()));
        onView(withId(R.id.SearchByName)).check(matches(isDisplayed()));
        onView(withId(R.id.InfoAboutTile)).check(matches(isDisplayed()));
        onView(withId(R.id.BoxVolume)).check(matches(isDisplayed()));
        onView(withId(R.id.BoxSquare)).check(matches(isDisplayed()));
        onView(withId(R.id.TileInBox)).check(matches(isDisplayed()));
        onView(withId(R.id.TilesInBox)).check(matches(isDisplayed()));
        onView(withId(R.id.Square)).check(matches(isDisplayed()));
        onView(withId(R.id.SearchingSquare)).check(matches(isDisplayed()));
        onView(withId(R.id.RequiredPieces)).check(matches(isDisplayed()));
        onView(withId(R.id.SearchingTiles)).check(matches(isDisplayed()));
        onView(withId(R.id.CalculateByM)).check(matches(isDisplayed()));
        onView(withId(R.id.CalculateByPieces)).check(matches(isDisplayed()));
        onView(withId(R.id.CalculateByPack)).check(matches(isDisplayed()));
        onView(withId(R.id.TileSquare)).check(matches(isDisplayed()));
        onView(withId(R.id.Result)).check(matches(isDisplayed()));
        onView(withId(R.id.PackagingBox)).check(matches(isDisplayed()));
        onView(withId(R.id.BoxCount)).check(matches(isDisplayed()));
        onView(withId(R.id.PackInfo)).check(matches(isDisplayed()));
        onView(withId(R.id.TileCount)).check(matches(isDisplayed()));
        onView(withId(R.id.TilesInfoName)).check(matches(isDisplayed()));
        onView(withId(R.id.ShowHistory)).check(matches(isDisplayed()));
    }

    @Test
    public void checkSearchByArticle() {
        onView(withId(R.id.SearchingArticle)).perform(typeText("0"), closeSoftKeyboard());
        onView(withId(R.id.SearchFromTreeMap)).perform(click());
        onView(withId(R.id.InfoAboutTile)).check(matches(withText("name")));
    }

    @Test
    public void checkSearchByName() {
        onView(withId(R.id.SearchingText)).perform(typeText("name"), closeSoftKeyboard());
        onView(withId(R.id.SearchByName)).perform(click());
        onView(withId(R.id.InfoAboutTile)).check(matches(withText("name")));
    }

    @Test
    public void checkCalculateByMeters() {
        onView(withId(R.id.BoxSquare)).perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.TilesInBox)).perform(typeText("9"), closeSoftKeyboard());
        onView(withId(R.id.SearchingSquare)).perform(typeText("5.5"), closeSoftKeyboard());
        onView(withId(R.id.CalculateByM)).perform(click());
        onView(withId(R.id.Result)).check(matches(withText("5.556")));
        onView(withId(R.id.BoxCount)).check(matches(withText("5")));
        onView(withId(R.id.TileCount)).check(matches(withText("5")));
        onView(withId(R.id.SearchingTiles)).check(matches(withText("50")));
    }

    @Test
    public void checkCalculateByPieces() {
        onView(withId(R.id.BoxSquare)).perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.TilesInBox)).perform(typeText("9"), closeSoftKeyboard());
        onView(withId(R.id.SearchingTiles)).perform(typeText("50"), closeSoftKeyboard());
        onView(withId(R.id.CalculateByPieces)).perform(click());
        onView(withId(R.id.Result)).check(matches(withText("5.556")));
        onView(withId(R.id.BoxCount)).check(matches(withText("5")));
        onView(withId(R.id.TileCount)).check(matches(withText("5")));
    }

    @Test
    public void checkCalculateByPacks() {
        onView(withId(R.id.BoxSquare)).perform(typeText("1.111"), closeSoftKeyboard());
        onView(withId(R.id.SearchingSquare)).perform(typeText("5"), closeSoftKeyboard());
        onView(withId(R.id.CalculateByPack)).perform(click());
        onView(withId(R.id.Result)).check(matches(withText("5.555")));
        onView(withId(R.id.BoxCount)).check(matches(withText("5")));
        onView(withId(R.id.TileCount)).check(matches(withText("0")));
    }

}
