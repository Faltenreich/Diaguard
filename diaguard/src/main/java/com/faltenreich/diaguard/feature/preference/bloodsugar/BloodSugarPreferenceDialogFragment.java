package com.faltenreich.diaguard.feature.preference.bloodsugar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.preference.EditTextPreferenceDialogFragmentCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.edittext.LocalizedNumberEditText;

public class BloodSugarPreferenceDialogFragment extends EditTextPreferenceDialogFragmentCompat {

    public static BloodSugarPreferenceDialogFragment newInstance(String key) {
        BloodSugarPreferenceDialogFragment fragment = new BloodSugarPreferenceDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_KEY, key);
        fragment.setArguments(arguments);
        return fragment;
    }

    private LocalizedNumberEditText editTextValue;

    private BloodSugarPreferenceDialogFragment() {
        super();
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        editTextValue = view.findViewById(android.R.id.edit);

        BloodSugarPreference preference = (BloodSugarPreference) getPreference();
        float value = FloatUtils.parseNumber(preference.getValue());
        value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, value);
        editTextValue.setText(FloatUtils.parseFloat(value));
        editTextValue.setSelection(editTextValue.getText() != null ? editTextValue.getText().length() : 0);

        TextView textViewUnit = view.findViewById(R.id.unit);
        textViewUnit.setText(PreferenceHelper.getInstance().getUnitName(Category.BLOODSUGAR));
    }

    /*
    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        final AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog == null || alertDialog.getButton(AlertDialog.BUTTON_POSITIVE) == null)
            throw new Resources.NotFoundException();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.validateEditTextEvent(context, editTextValue, Category.BLOODSUGAR, true)) {
                    alertDialog.dismiss();
                    onDialogClosed(true);
                }
            }
        });
    }
    */

    @Override
    public void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult && editTextValue != null && editTextValue.getText() != null) {
            float value = FloatUtils.parseNumber(editTextValue.getText().toString());
            value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(Category.BLOODSUGAR, value);
            BloodSugarPreference preference = (BloodSugarPreference) getPreference();
            preference.setValue(Float.toString(value));
        }
    }
}