package com.faltenreich.diaguard.preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.ViewHelper;

import java.util.Calendar;

/**
 * Created by Filip on 04.11.13.
 */
public class CleanUpPreference extends DialogPreference {

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;
    Activity activity;

    DatePicker datePicker;

    public CleanUpPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (Activity) context;

        setDialogLayoutResource(R.layout.preference_cleanup);

        dataSource = new DatabaseDataSource(activity);
        preferenceHelper = new PreferenceHelper(activity);
    }

    private void cleanUp(Calendar date) {

        dataSource.open();
        int count = dataSource.deleteEventsBefore(date);
        dataSource.close();

        ViewHelper.showInfo(activity, count + " " +
                activity.getResources().getString(R.string.pref_data_cleanup_return));
    }

    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);

        datePicker = (DatePicker) view.findViewById(R.id.datepicker);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {

        if(positiveResult) {

            final Calendar date = Calendar.getInstance();
            date.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

            dataSource.open();
            int count = dataSource.countEventsBefore(date);
            dataSource.close();

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            dialogBuilder
                    .setTitle(activity.getResources().getString(R.string.pref_data_cleanup_confirm_title))
                    .setMessage(count + " " + activity.getResources().getString(R.string.pref_data_cleanup_confirm_message))
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            cleanUp(date);
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = dialogBuilder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }
}