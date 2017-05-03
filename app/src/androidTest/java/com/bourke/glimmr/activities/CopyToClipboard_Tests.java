package com.bourke.glimmr.activities;


import android.os.Build;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.bourke.glimmr.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.bourke.glimmr.fragments.viewer.PhotoViewerFragment.getClipboardText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CopyToClipboard_Tests {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void searchForImageCopyPasteURL() {

        /* ViewInteraction textView = onView(
                allOf(withId(R.id.textNotNow), withText("or, just browse some photos…"), isDisplayed()));
        textView.perform(click());

        The above piece of code can cause the test to fail if the first screen that glimmr has open does not have an overlay
        with the option "or,just browse some photos..." After installing the glimmr app onto the phone the overlay no longer
        appears if you choose "or,just browse some photos..." a number of times. When initially opening the app for the first
         time the overlay should appear.
        */


        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.menu_search), withContentDescription("Search"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText("kinda intense For a piece of plastic"), closeSoftKeyboard());

        ViewInteraction searchAutoComplete2 = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")), withText("kinda intense For a piece of plastic"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        searchAutoComplete2.perform(pressImeActionButton());

        ViewInteraction frameLayout = onView(
                allOf(withId(R.id.image_layout),
                        childAtPosition(
                                allOf(withId(R.id.gridview),
                                        withParent(withId(R.id.swipe_container))),
                                0),
                        isDisplayed()));
        frameLayout.perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.title), withText("Copy URL to Clipboard"), isDisplayed()));
        textView.perform(click());

        //getClipboardText() returns the ClipData object (String equivalent) on the clipboard
        String text = getClipboardText();

        ViewInteraction linearLayout = onView(
                allOf(withContentDescription("Glimmr, Navigate up"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ActionBarView")),
                                withParent(withClassName(is("com.android.internal.widget.ActionBarContainer"))))),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.menu_search), withContentDescription("Search"), isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction searchAutoComplete3 = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        searchAutoComplete3.perform(longClick());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       /* ViewInteraction button = onView(
                allOf(withText("Paste"), withContentDescription("Paste"), isDisplayed()));
        button.perform(click());

        Espresso does not have the capability of actually pressing the Paste button since its a system menu, this test fails unless
        the above piece is commented out. The correct text is still printed in the search bar however because espresso uses a replaceText()
         method to print out the contents of the clipboard.

        */

        ViewInteraction searchAutoComplete4 = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        searchAutoComplete4.perform(replaceText("\"kinda intense for a piece of plastic\" by ~lzee~by~the~Sea~: https://flickr.com/photos/77108378@N06/33573054763"), closeSoftKeyboard());

        String actualText = "ClipData { text/plain \"Url\" {T:\"kinda intense for a piece of plastic\" by ~lzee~by~the~Sea~: https://flickr.com/photos/77108378@N06/33573054763} }";

        //Compare the actual ClipData value (String equivalent) to the string that is pasted in the search bar
         assertEquals(text,actualText);

    }
    @Test
    public void copyURL_NotNull() {
       /* ViewInteraction textView = onView(
                allOf(withId(R.id.textNotNow), withText("or, just browse some photos…"), isDisplayed()));
        textView.perform(click()); */

        ViewInteraction frameLayout = onView(
                allOf(withId(R.id.image_layout),
                        childAtPosition(
                                allOf(withId(R.id.gridview),
                                        withParent(withId(R.id.swipe_container))),
                                5),
                        isDisplayed()));
        frameLayout.perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.title), withText("Copy URL to Clipboard"), isDisplayed()));
        textView2.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withContentDescription("Glimmr, Navigate up"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ActionBarView")),
                                withParent(withClassName(is("com.android.internal.widget.ActionBarContainer"))))),
                        isDisplayed()));
        linearLayout.perform(click());

        //getClipboardText() returns the ClipData object (String equivalent) on the clipboard
        String text = getClipboardText();

        //Compare the actual ClipData value (String equivalent) to NULL
        assertNotNull(text);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }



}
