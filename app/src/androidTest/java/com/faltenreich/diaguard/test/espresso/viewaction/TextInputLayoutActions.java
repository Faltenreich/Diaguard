package com.faltenreich.diaguard.test.espresso.viewaction;

import android.view.View;
import android.widget.EditText;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import java.util.Locale;

public class TextInputLayoutActions {

    public static ViewAction replaceText(String text) {
        return ViewActions.actionWithAssertions(new ReplaceTextAction(text));
    }

    static class ReplaceTextAction implements ViewAction {

        private final String text;

        ReplaceTextAction(String text) {
            this.text = text;
        }

        @Override
        public String getDescription() {
            return String.format(Locale.ROOT, "replace text(%s)", text);
        }

        @Override
        public Matcher<View> getConstraints() {
            return CoreMatchers.allOf(ViewMatchers.isDisplayed(), ViewMatchers.isAssignableFrom(TextInputLayout.class));
        }

        @Override
        public void perform(UiController uiController, View view) {
            if (view instanceof TextInputLayout) {
                TextInputLayout textInputLayout = (TextInputLayout) view;
                EditText editText = textInputLayout.getEditText();
                if (editText != null) {
                    editText.setText(text);
                }
            }
        }
    }
}