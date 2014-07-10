package com.faltenreich.diaguard.views;

import android.content.Context;
import android.text.Editable;
import android.widget.EditText;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.Event;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.Validator;

/**
 * Created by Filip on 01.06.2014.
 */
public class EventEditText extends EditText {

    public Event.Category category;

    public EventEditText(Context context, Event.Category category) {
        super(context);
        this.category = category;
    }

    public boolean isValid() {
        Editable editable = this.getText();
        if(editable == null) {
            throw new IllegalArgumentException();
        }
        else {
            String value = editable.toString();
            if (value.length() == 0) {
                // Check for Hint values
                CharSequence charSequence = this.getHint();
                if(charSequence != null && charSequence.toString().length() > 0 ) {
                    return hasValidValue(charSequence.toString());
                }
                else {
                    setError(getContext().getString(R.string.validator_value_empty));
                    return false;
                }
            }
            else {
                setError(null);
                return (hasValidValue(value));
            }
        }
    }

    private boolean hasValidValue(String value) {
        PreferenceHelper preferenceHelper = new PreferenceHelper(getContext());

        if (!Validator.containsNumber(value)) {
            setError(getContext().getString(R.string.validator_value_number));
            return false;
        }
        else if (!preferenceHelper.validateEventValue(category,
                preferenceHelper.formatCustomToDefaultUnit(category,
                        Float.parseFloat(value)))) {
            setError(getContext().getString(R.string.validator_value_unrealistic));
            return false;
        }
        return true;
    }
}
