package com.faltenreich.diaguard.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.Validator;

/**
 * Created by Filip on 04.11.13.
 */
public class FactorPreference extends DialogPreference {

    public final static String FACTOR = "factor_";

    Context context;
    SharedPreferences sharedPreferences;

    private EditText morning;
    private EditText noon;
    private EditText evening;
    private EditText night;

    public FactorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_factor);

        this.context = context;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);

        morning = (EditText) view.findViewById(R.id.morning);
        noon = (EditText) view.findViewById(R.id.noon);
        evening = (EditText) view.findViewById(R.id.evening);
        night = (EditText) view.findViewById(R.id.night);

        if(sharedPreferences.contains(FACTOR + PreferenceHelper.Daytime.Morning)) {
            morning.setText(Helper.getDecimalFormat().format(
                    sharedPreferences.getFloat(FACTOR + PreferenceHelper.Daytime.Morning, 1)));
            morning.setSelection(morning.length());
        }

        if(sharedPreferences.contains(FACTOR + PreferenceHelper.Daytime.Noon)) {
            noon.setText(Helper.getDecimalFormat().format(
                    sharedPreferences.getFloat(FACTOR + PreferenceHelper.Daytime.Noon, 1)));
            noon.setSelection(noon.length());
        }

        if(sharedPreferences.contains(FACTOR + PreferenceHelper.Daytime.Evening)) {
            evening.setText(Helper.getDecimalFormat().format(
                    sharedPreferences.getFloat(FACTOR + PreferenceHelper.Daytime.Evening, 1)));
            evening.setSelection(evening.length());
        }

        if(sharedPreferences.contains(FACTOR + PreferenceHelper.Daytime.Night)) {
            night.setText(Helper.getDecimalFormat().format(
                    sharedPreferences.getFloat(FACTOR + PreferenceHelper.Daytime.Night, 1)));
            night.setSelection(night.length());
        }

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
                boolean isValid = true;

                if(!Validator.validateEditTextFactor(context, morning, true))
                    isValid = false;
                if(!Validator.validateEditTextFactor(context, noon, true))
                    isValid = false;
                if(!Validator.validateEditTextFactor(context, evening, true))
                    isValid = false;
                if(!Validator.validateEditTextFactor(context, night, true))
                    isValid = false;

                if(isValid)
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
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(morning.length() == 0)
                editor.remove(FACTOR + PreferenceHelper.Daytime.Morning);
            else
                editor.putFloat(FACTOR + PreferenceHelper.Daytime.Morning, Float.parseFloat(morning.getText().toString()));

            if(noon.length() == 0)
                editor.remove(FACTOR + PreferenceHelper.Daytime.Noon);
            else
                editor.putFloat(FACTOR + PreferenceHelper.Daytime.Noon, Float.parseFloat(noon.getText().toString()));

            if(evening.length() == 0)
                editor.remove(FACTOR + PreferenceHelper.Daytime.Evening);
            else
                editor.putFloat(FACTOR + PreferenceHelper.Daytime.Evening, Float.parseFloat(evening.getText().toString()));

            if(night.length() == 0)
                editor.remove(FACTOR + PreferenceHelper.Daytime.Night);
            else
                editor.putFloat(FACTOR + PreferenceHelper.Daytime.Night, Float.parseFloat(night.getText().toString()));

            editor.commit();
        }
    }
}