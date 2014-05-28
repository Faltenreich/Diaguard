package com.android.diaguard.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.android.diaguard.R;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;

/**
 * Created by Filip on 04.11.13.
 */
public class FactorPreference extends DialogPreference {

    public final static String FACTOR = "factor_";

    Activity activity;
    PreferenceHelper preferenceHelper;
    SharedPreferences sharedPreferences;

    private EditText morning;
    private EditText noon;
    private EditText evening;
    private EditText night;

    public FactorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_factor);

        activity = (Activity) context;

        preferenceHelper = new PreferenceHelper(activity);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
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