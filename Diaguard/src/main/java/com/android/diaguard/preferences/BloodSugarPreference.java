package com.android.diaguard.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.diaguard.R;
import com.android.diaguard.database.Event;
import com.android.diaguard.helpers.PreferenceHelper;
import com.android.diaguard.helpers.Validator;

/**
 * Created by Filip on 04.11.13.
 */
public class BloodSugarPreference extends EditTextPreference {

    Activity activity;
    SharedPreferences sharedPreferences;

    EditText editTextValue;
    TextView textViewUnit;

    public BloodSugarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_bloodsugar);

        activity = (Activity) context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);

        PreferenceHelper preferenceHelper = new PreferenceHelper(activity);

        editTextValue = (EditText) view.findViewById(R.id.value);
        float value = Float.parseFloat(sharedPreferences.getString(getKey(), ""));
        value = preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, value);
        editTextValue.setText(preferenceHelper.getDecimalFormat(Event.Category.BloodSugar).format(value));
        editTextValue.setSelection(editTextValue.getText().length());

        textViewUnit = (TextView) view.findViewById(R.id.unit);
        textViewUnit.setText(preferenceHelper.getUnitName(Event.Category.BloodSugar));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            PreferenceHelper preferenceHelper = new PreferenceHelper(activity);

            float value = Float.valueOf(editTextValue.getText().toString());
            if(Validator.validateEventValue(activity, Event.Category.BloodSugar,
                    preferenceHelper.formatCustomToDefaultUnit(Event.Category.BloodSugar, value))) {
                SharedPreferences.Editor editor = getEditor();
                value = preferenceHelper.formatCustomToDefaultUnit(Event.Category.BloodSugar, value);
                editor.putString(getKey(), Float.toString(value));
                editor.commit();
            }
        }
    }
}