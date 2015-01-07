package com.faltenreich.diaguard.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.Validator;

/**
 * Created by Filip on 04.11.13.
 */
public class BloodSugarPreference extends EditTextPreference {

    private Context context;
    private SharedPreferences sharedPreferences;
    private PreferenceHelper preferenceHelper;

    private EditText editTextValue;

    public BloodSugarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_bloodsugar);

        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        this.preferenceHelper = new PreferenceHelper(context);
    }

    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);

        editTextValue = (EditText) view.findViewById(R.id.value);
        if(editTextValue == null || editTextValue.getText() == null)
            throw new Resources.NotFoundException();

        float value = Float.parseFloat(sharedPreferences.getString(getKey(), ""));
        value = preferenceHelper.formatDefaultToCustomUnit(Measurement.Category.BloodSugar, value);
        editTextValue.setText(preferenceHelper.getDecimalFormat(Measurement.Category.BloodSugar).format(value));
        editTextValue.setSelection(editTextValue.getText().length());

        TextView textViewUnit = (TextView) view.findViewById(R.id.unit);
        textViewUnit.setText(preferenceHelper.getUnitName(Measurement.Category.BloodSugar));
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        final AlertDialog alertDialog = (AlertDialog)getDialog();
        if(alertDialog == null || alertDialog.getButton(AlertDialog.BUTTON_POSITIVE) == null)
            throw new Resources.NotFoundException();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(Validator.validateEditTextEvent(context, editTextValue, Measurement.Category.BloodSugar))
                {
                    alertDialog.dismiss();
                    onDialogClosed(true);
                }
            }
        });
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            if(editTextValue == null || editTextValue.getText() == null)
                throw new Resources.NotFoundException();

            float value = Float.valueOf(editTextValue.getText().toString());
            SharedPreferences.Editor editor = getEditor();
            if(editor == null)
                throw new Resources.NotFoundException();

            value = preferenceHelper.formatCustomToDefaultUnit(Measurement.Category.BloodSugar, value);
            editor.putString(getKey(), Float.toString(value));
            editor.commit();
        }
    }
}