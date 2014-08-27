package com.faltenreich.diaguard.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.widget.EditText;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.Measurement;

/**
 * Created by Filip on 05.11.13.
 */
public class Validator {

    public static boolean containsNumber(String input) {
        return input.matches(".*\\d.*");
    }

    public static boolean validateEditTextEvent(Context context, EditText editText, Measurement.Category category) {
        Editable editable = editText.getText();

        if(editable == null) {
            throw new Resources.NotFoundException();
        }

        String value = editable.toString();
        if (value.length() > 0) {
            return validateEventValue(context, editText, category, value);
        }

        else {
            // Check for Hint value
            CharSequence charSequence = editText.getHint();

            if(charSequence != null && charSequence.toString().length() > 0 ) {
                return validateEventValue(context, editText, category, charSequence.toString());
            }

            else {
                editText.setError(context.getString(R.string.validator_value_empty));
                return false;
            }
        }
    }

    public static boolean validateEventValue(Context context, EditText editText, Measurement.Category category, String value) {
        PreferenceHelper preferenceHelper = new PreferenceHelper(context);

        if (!containsNumber(value)) {
            editText.setError(context.getString(R.string.validator_value_number));
            return false;
        }

        float parsedValue = Float.parseFloat(value);
        float defaultValue = preferenceHelper.formatCustomToDefaultUnit(category, parsedValue);

        if (!preferenceHelper.validateEventValue(category, defaultValue)) {
            editText.setError(context.getString(R.string.validator_value_unrealistic));
            return false;
        }

        editText.setError(null);
        return true;
    }

    public static boolean validateEditTextFactor(Context context, EditText editText, boolean canBeEmpty) {
        Editable editable = editText.getText();

        if (editable == null) {
            throw new IllegalArgumentException();
        }

        String value = editable.toString();
        if (value.length() > 0) {
            return validateFactor(context, editText, value);
        }

        else {

            if(canBeEmpty) {
                return true;
            }

            else {
                // Check for Hint value
                CharSequence charSequence = editText.getHint();

                if(charSequence != null && charSequence.toString().length() > 0 ) {
                    return validateFactor(context, editText, charSequence.toString());
                }

                else {
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

        float parsedValue = Float.parseFloat(value);
        if(parsedValue < 0.1f || parsedValue > 20) {
            editText.setError(context.getString(R.string.validator_value_unrealistic));
            return false;
        }

        editText.setError(null);
        return true;
    }
}