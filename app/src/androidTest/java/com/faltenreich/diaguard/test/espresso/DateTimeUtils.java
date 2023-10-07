package com.faltenreich.diaguard.test.espresso;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.faltenreich.diaguard.test.espresso.viewaction.TextInputLayoutActions;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

public class DateTimeUtils {

    public static void setDate(LocalDate date){
        Espresso.onView(ViewMatchers.withTagValue((Matchers.is("TOGGLE_BUTTON_TAG"))))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.mtrl_picker_text_input_date))
            .perform(TextInputLayoutActions.replaceText(DateTimeFormat.forPattern("M/d/yy").print(date)));
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.confirm_button))
            .perform(ViewActions.click());
    }

    public static void setDateRange(LocalDate start, LocalDate end){
        Espresso.onView(ViewMatchers.withTagValue((Matchers.is("TOGGLE_BUTTON_TAG"))))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.mtrl_picker_text_input_range_start))
            .perform(TextInputLayoutActions.replaceText(DateTimeFormat.forPattern("M/d/yy").print(start)));
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.mtrl_picker_text_input_range_end))
            .perform(TextInputLayoutActions.replaceText(DateTimeFormat.forPattern("M/d/yy").print(end)));
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.confirm_button))
            .perform(ViewActions.click());
    }

    // FIXME:
    //  java.lang.RuntimeException: Action will not be performed because the target view does not match one or more of the following constraints:
    //  (view has effective visibility <VISIBLE> and view.getGlobalVisibleRect() covers at least <90> percent of the view's area)
    public static void setTime(LocalTime time){
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.material_timepicker_mode_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.material_hour_text_input))
            .perform(ViewActions.click());
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withClassName(CoreMatchers.is(AppCompatEditText.class.getName()))))
            .perform(ViewActions.replaceText(DateTimeFormat.forPattern("hh").print(time)));
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.material_minute_text_input))
            .perform(ViewActions.click());
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withClassName(CoreMatchers.is(AppCompatEditText.class.getName()))))
            .perform(ViewActions.replaceText(DateTimeFormat.forPattern("mm").print(time)));
    }
}
