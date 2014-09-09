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
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.ViewHelper;

import org.joda.time.DateTime;

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

    private void cleanUp(DateTime date) {
        dataSource.open();
        String dateString = Helper.getDateDatabaseFormat().print(date.withTime(23, 59, 59, 999));
        int count = dataSource.delete(DatabaseHelper.ENTRY,
                DatabaseHelper.DATE + "<=DateTime(?)", new String[]{ dateString });
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
            final DateTime date = new DateTime()
                    .withYear(datePicker.getYear())
                    .withMonthOfYear(datePicker.getMonth()+1)
                    .withDayOfMonth(datePicker.getDayOfMonth());

            dataSource.open();
            int count = dataSource.countEntriesBefore(date);
            dataSource.close();

            if(count <= 0)
                return;

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            dialogBuilder
                    .setTitle(activity.getResources().getString(R.string.pref_data_cleanup_confirm_title))
                    .setMessage(count + " " +
                            activity.getString(R.string.pref_data_cleanup_confirm_message))
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