package com.faltenreich.diaguard.ui.view.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.NumberUtils;
import com.faltenreich.diaguard.util.Validator;

/**
 * Created by Filip on 04.11.13.
 */
public class BloodSugarPreference extends EditTextPreference {

    private Context context;
    private SharedPreferences sharedPreferences;

    private EditText editTextValue;

    public BloodSugarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_bloodsugar);

        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);

        editTextValue = (EditText) view.findViewById(R.id.value);
        if(editTextValue == null || editTextValue.getText() == null)
            throw new Resources.NotFoundException();

        float value = NumberUtils.parseNumber(sharedPreferences.getString(getKey(), ""));
        value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, value);
        editTextValue.setText(Helper.parseFloat(value));
        editTextValue.setSelection(editTextValue.getText().length());

        TextView textViewUnit = (TextView) view.findViewById(R.id.unit);
        textViewUnit.setText(PreferenceHelper.getInstance().getUnitName(Measurement.Category.BLOODSUGAR));

        if(Build.VERSION.SDK_INT <= 10) {
            view.setBackgroundColor(Color.WHITE);
        }
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
                if (Validator.validateEditTextEvent(context, editTextValue, Measurement.Category.BLOODSUGAR, true)) {
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

            float value = NumberUtils.parseNumber(editTextValue.getText().toString());
            SharedPreferences.Editor editor = getEditor();
            if(editor == null)
                throw new Resources.NotFoundException();

            value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(Measurement.Category.BLOODSUGAR, value);
            editor.putString(getKey(), Float.toString(value));
            editor.commit();
        }
    }
}