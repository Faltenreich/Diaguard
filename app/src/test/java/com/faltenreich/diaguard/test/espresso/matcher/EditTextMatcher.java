package com.faltenreich.diaguard.test.espresso.matcher;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.StringRes;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class EditTextMatcher {

    public static Matcher<View> hasErrorText(@StringRes int errorTextRes) {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String errorText = context.getString(errorTextRes);
        return ViewMatchers.hasErrorText(errorText);
    }

    public static Matcher<View> hasNoErrorText() {
        return new BoundedMatcher<View, EditText>(EditText.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has no error text: ");
            }
            @Override
            protected boolean matchesSafely(EditText view) {
                return view.getError() == null;
            }
        };
    }
}
