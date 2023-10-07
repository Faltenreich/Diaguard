package com.faltenreich.diaguard.feature.entry.edit;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.test.junit.rule.TestRuleFactory;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EntryEditTagTest {

    @Rule
    public final TestRule rule = TestRuleFactory.forFragment(EntryEditFragment.class);

    @Test
    public void creatingTagViaKeyboardInput_shouldAppendToList() {
        String tag = "new tag";
        Espresso.onView(ViewMatchers.withId(R.id.tag_input)).perform(
            ViewActions.click(),
            ViewActions.replaceText(tag),
            ViewActions.pressImeActionButton()
        );
        Espresso.onView(ViewMatchers.withText(tag))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
