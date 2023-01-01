package com.faltenreich.diaguard.shared.data.validation;

import android.content.Context;
import android.text.Editable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Created by Filip on 05.11.13.
 */
public class Validator {

    public static boolean containsNumber(String input) {
        return input.matches(".*\\d.*");
    }

    public static boolean validateEditTextEvent(Context context, TextInputLayout inputView, Category category, boolean checkForHint) {
        if (inputView.getEditText() == null || inputView.getEditText().getText() == null) {
            return false;
        }

        String value = inputView.getEditText().getText().toString();

        if (!StringUtils.isBlank(value)) {
            return validateEventValue(context, inputView, category, value);

        } else if (checkForHint) {
            // Check for Hint value
            CharSequence hint = inputView.getHint();
            if (hint != null && hint.toString().length() > 0) {
                return validateEventValue(context, inputView, category, hint.toString());

            } else {
                inputView.setError(context.getString(R.string.validator_value_empty));
                return false;
            }

        } else {
            inputView.setError(context.getString(R.string.validator_value_empty));
            return false;
        }
    }

    private static boolean validateEventValue(Context context, TextInputLayout inputView, Category category, String value) {
        inputView.setError(null);

        boolean isValid = true;
        if (containsNumber(value)) {
            float parsedValue = FloatUtils.parseNumber(value);
            float defaultValue = PreferenceStore.getInstance().formatCustomToDefaultUnit(category, parsedValue);
            if (!PreferenceStore.getInstance().validateEventValue(category, defaultValue)) {
                inputView.setError(context.getString(R.string.validator_value_unrealistic));
                isValid = false;
            }
        } else {
            inputView.setError(context.getString(R.string.validator_value_number));
            isValid = false;
        }
        return isValid;
    }

    public static boolean validateEditTextFactor(Context context, TextInputLayout inputView, boolean canBeEmpty) {
        Editable editable = inputView.getEditText().getText();

        if (editable == null) {
            throw new IllegalArgumentException();
        }

        String value = editable.toString();
        if (value.length() > 0) {
            return validateFactor(context, inputView, value);
        } else {

            if (canBeEmpty) {
                return true;
            } else {
                // Check for Hint value
                CharSequence charSequence = inputView.getHint();

                if (charSequence != null && charSequence.toString().length() > 0) {
                    return validateFactor(context, inputView, charSequence.toString());
                } else {
                    inputView.setError(context.getString(R.string.validator_value_empty));
                    return false;
                }
            }
        }
    }

    private static boolean validateFactor(Context context, TextInputLayout inputView, String value) {
        if (!containsNumber(value)) {
            inputView.setError(context.getString(R.string.validator_value_number));
            return false;
        }

        float parsedValue = FloatUtils.parseNumber(value);
        if (parsedValue < 0.1f || parsedValue > 20) {
            inputView.setError(context.getString(R.string.validator_value_unrealistic));
            return false;
        }

        inputView.setError(null);
        return true;
    }
}