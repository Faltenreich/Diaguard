package com.faltenreich.diaguard.util;

import android.content.Context;
import android.text.Editable;
import android.widget.EditText;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;

/**
 * Created by Filip on 05.11.13.
 */
public class Validator {

    public static boolean containsNumber(String input) {
        return input.matches(".*\\d.*");
    }

    public static boolean validateEditTextEvent(Context context, EditText editText, Measurement.Category category, boolean checkForHint) {
        String value = editText.getText().toString();

        if (value.length() > 0) {
            return validateEventValue(context, editText, category, value);

        } else if (checkForHint) {
            // Check for Hint value
            CharSequence hint = editText.getHint();
            if (hint != null && hint.toString().length() > 0) {
                return validateEventValue(context, editText, category, hint.toString());

            } else {
                editText.setError(context.getString(R.string.validator_value_empty));
                return false;
            }

        } else {
            editText.setError(context.getString(R.string.validator_value_empty));
            return false;
        }
    }

    public static boolean validateEventValue(Context context, EditText editText, Measurement.Category category, String value) {
        editText.setError(null);

        boolean isValid = true;
        if (containsNumber(value)) {
            float parsedValue = NumberUtils.parseNumber(value);
            float defaultValue = PreferenceHelper.getInstance().formatCustomToDefaultUnit(category, parsedValue);
            if (!PreferenceHelper.getInstance().validateEventValue(category, defaultValue)) {
                editText.setError(context.getString(R.string.validator_value_unrealistic));
                isValid = false;
            }
        } else {
            editText.setError(context.getString(R.string.validator_value_number));
            isValid = false;
        }
        return isValid;
    }

    public static boolean validateEditTextFactor(Context context, EditText editText, boolean canBeEmpty) {
        Editable editable = editText.getText();

        if (editable == null) {
            throw new IllegalArgumentException();
        }

        String value = editable.toString();
        if (value.length() > 0) {
            return validateFactor(context, editText, value);
        } else {

            if (canBeEmpty) {
                return true;
            } else {
                // Check for Hint value
                CharSequence charSequence = editText.getHint();

                if (charSequence != null && charSequence.toString().length() > 0) {
                    return validateFactor(context, editText, charSequence.toString());
                } else {
                    editText.setError(context.getString(R.string.validator_value_empty));
                    return false;
                }
            }
        }
    }

    public static boolean validateFactor(Context context, EditText editText, String value) {
        if (!containsNumber(value)) {
            editText.setError(context.getString(R.string.validator_value_number));
            return false;
        }

        float parsedValue = NumberUtils.parseNumber(value);
        if (parsedValue < 0.1f || parsedValue > 20) {
            editText.setError(context.getString(R.string.validator_value_unrealistic));
            return false;
        }

        editText.setError(null);
        return true;
    }
}