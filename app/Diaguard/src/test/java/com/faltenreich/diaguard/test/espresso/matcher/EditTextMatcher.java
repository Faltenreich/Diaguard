package com.faltenreich.diaguard.test.espresso.matcher;

import android.content.Context;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;

public class EditTextMatcher {

    public static Matcher<View> hasErrorText(@StringRes int errorTextRes) {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String errorText = context.getString(errorTextRes);
        return ViewMatchers.hasErrorText(errorText);
    }
}
