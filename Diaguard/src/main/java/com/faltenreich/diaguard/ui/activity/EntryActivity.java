package com.faltenreich.diaguard.ui.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.event.data.EntryUpdatedEvent;
import com.faltenreich.diaguard.ui.fragment.BaseFoodFragment;
import com.faltenreich.diaguard.ui.fragment.DatePickerFragment;
import com.faltenreich.diaguard.ui.fragment.TimePickerFragment;
import com.faltenreich.diaguard.ui.view.entry.MeasurementFloatingActionMenu;
import com.faltenreich.diaguard.ui.view.entry.MeasurementListView;
import com.faltenreich.diaguard.util.AlarmUtils;
import com.faltenreich.diaguard.util.DateTimeUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.ViewUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Filip on 19.10.13.
 */
public class EntryActivity extends BaseActivity implements MeasurementFloatingActionMenu.OnFabSelectedListener, MeasurementListView.OnCategoryEventListener {

    public static final String EXTRA_ENTRY = "EXTRA_ENTRY";
    public static final String EXTRA_DATE = "EXTRA_DATE";

    @BindView(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.activity_newevent_scrollview) NestedScrollView scrollView;
    @BindView(R.id.fab_menu) MeasurementFloatingActionMenu fab;
    @BindView(R.id.layout_measurements) MeasurementListView layoutMeasurements;
    @BindView(R.id.edittext_notes) EditText editTextNotes;
    @BindView(R.id.button_date) Button buttonDate;
    @BindView(R.id.button_time) Button buttonTime;
    @BindView(R.id.entry_button_alarm) Button buttonAlarm;

    private Entry entry;
    private DateTime time;
    private int alarmInMinutes;

    public EntryActivity() {
        super(R.layout.activity_entry);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_edit, menu);
        menu.findItem(R.id.action_delete).setVisible(entry != null);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteEntry();
                return true;
            case R.id.action_done:submit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initialize() {
        time = new DateTime();

        layoutMeasurements.setOnCategoryEventListener(this);

        fab.init();
        fab.setOnFabSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getLong(EXTRA_ENTRY) != 0L) {
                setTitle(getString(R.string.entry_edit));
                new FetchEntryTask(extras.getLong(EXTRA_ENTRY)).execute();

            } else if (extras.getSerializable(BaseFoodFragment.EXTRA_FOOD_ID) != null) {
                new FetchFoodTask(extras.getLong(BaseFoodFragment.EXTRA_FOOD_ID)).execute();
                updateDateTime();

            } else if (extras.getSerializable(EXTRA_DATE) != null) {
                time = (DateTime) extras.getSerializable(EXTRA_DATE);
                initPinnedCategories();
                updateDateTime();

            } else {
                updateDateTime();
            }

        } else {
            initPinnedCategories();
            updateDateTime();
        }

        updateAlarm();
    }

    private void initPinnedCategories() {
        for (Measurement.Category category : PreferenceHelper.getInstance().getActiveCategories()) {
            if (PreferenceHelper.getInstance().isCategoryPinned(category) && !layoutMeasurements.hasCategory(category)) {
                layoutMeasurements.addMeasurementAtEnd(category);
            }
        }
    }

    private void initEntry(Entry entry) {
        if (entry != null) {
            this.entry = entry;
            this.time = entry.getDate();

            editTextNotes.setText(entry.getNote());

            List<Measurement> measurements = entry.getMeasurementCache();
            layoutMeasurements.addMeasurements(measurements);

            for (Measurement measurement : measurements) {
                fab.ignore(measurement.getCategory());
            }
            fab.restock();

            updateDateTime();
        }
    }

    private void initFood(Food food) {
        if (food != null) {
            layoutMeasurements.addMeasurement(food);
            fab.ignore(Measurement.Category.MEAL);
            fab.restock();
        }
    }

    private void showDialogCategories() {
        final Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();

        String[] categoryNames = new String[activeCategories.length];
        boolean[] visibleCategoriesOld = new boolean[activeCategories.length];
        for (int position = 0; position < activeCategories.length; position++) {
            Measurement.Category category = activeCategories[position];
            categoryNames[position] = category.toLocalizedString();
            visibleCategoriesOld[position] = layoutMeasurements.hasCategory(category);
        }

        final boolean[] visibleCategories = visibleCategoriesOld.clone();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.categories)
                .setMultiChoiceItems(
                        categoryNames,
                        visibleCategoriesOld,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                visibleCategories[which] = isChecked;
                            }
                        }
                )
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int position = activeCategories.length - 1; position >= 0; position--) {
                            Measurement.Category category = activeCategories[position];
                            if (visibleCategories[position]) {
                                addMeasurementView(category);
                            } else {
                                removeMeasurementView(category);
                            }
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateDateTime() {
        buttonDate.setText(Helper.getDateFormat().print(time));
        buttonTime.setText(Helper.getTimeFormat().print(time));
    }

    private void updateAlarm() {
        buttonAlarm.setText(alarmInMinutes > 0 ?
                String.format("%s %s", getString(R.string.alarm_reminder_in), DateTimeUtils.parseInterval(alarmInMinutes * DateTimeConstants.MILLIS_PER_MINUTE)) :
                getString(R.string.alarm_reminder_none));
    }

    private void addMeasurementView(Measurement.Category category) {
        scrollView.smoothScrollTo(0, 0);
        layoutMeasurements.addMeasurement(category);
    }

    private void removeMeasurementView(Measurement.Category category) {
        layoutMeasurements.removeMeasurement(category);
    }

    private boolean inputIsValid() {
        boolean inputIsValid = true;

        // Validate date
        if (time.isAfter(DateTime.now())) {
            ViewUtils.showSnackbar(findViewById(android.R.id.content), getString(R.string.validator_value_infuture));
            inputIsValid = false;
        }

        // Check whether there are values to submit
        else if (layoutMeasurements.getMeasurements().size() == 0) {
            ViewUtils.showSnackbar(findViewById(android.R.id.content), getString(R.string.validator_value_none));
            inputIsValid = false;
        } else {
            for (Measurement measurement : layoutMeasurements.getMeasurements()) {
                if (measurement == null) {
                    inputIsValid = false;
                }
            }
        }

        return inputIsValid;
    }

    private void submit() {
        if (inputIsValid()) {
            boolean isNewEntry = entry == null;
            DateTime originalDate = isNewEntry ? null : entry.getDate();
            if (isNewEntry) {
                entry = new Entry();
            }

            entry.setDate(time);
            entry.setNote(editTextNotes.length() > 0 ? editTextNotes.getText().toString() : null);
            EntryDao.getInstance().createOrUpdate(entry);

            for (Measurement.Category category : Measurement.Category.values()) {

                if (layoutMeasurements.hasCategory(category)) {
                    Measurement measurement = layoutMeasurements.getMeasurement(category);
                    measurement.setEntry(entry);
                    MeasurementDao.getInstance(measurement.getClass()).createOrUpdate(measurement);
                } else {
                    MeasurementDao.getInstance(category.toClass()).deleteMeasurements(entry);
                }
            }

            if (alarmInMinutes > 0) {
                AlarmUtils.setAlarm(alarmInMinutes * DateTimeConstants.MILLIS_PER_MINUTE);
            }

            if (isNewEntry) {
                Toast.makeText(this, getString(R.string.entry_added), Toast.LENGTH_LONG).show();
                Events.post(new EntryAddedEvent(entry));
            } else {
                Events.post(new EntryUpdatedEvent(entry, originalDate));
            }

            finish();
        }
    }

    private void deleteEntry() {
        if (entry != null) {
            EntryDao.getInstance().delete(entry);
            Events.post(new EntryDeletedEvent(entry));
            finish();
        }
    }

    @OnClick(R.id.button_date)
    public void showDatePicker() {
        DatePickerFragment.newInstance(time, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time = time.withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day);
                updateDateTime();
            }
        }).show(getSupportFragmentManager());
    }

    @OnClick(R.id.button_time)
    public void showTimePicker() {
        TimePickerFragment.newInstance(time, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = time.withHourOfDay(hourOfDay).withMinuteOfHour(minute);
                updateDateTime();
            }
        }).show(getSupportFragmentManager());
    }

    @OnClick(R.id.entry_button_alarm)
    public void showAlarmPicker() {
        ViewUtils.showNumberPicker(this, R.string.minutes, alarmInMinutes, 0, 10000, new NumberPickerDialogFragment.NumberPickerDialogHandlerV2() {
            @Override
            public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
                alarmInMinutes = number.intValue();
                updateAlarm();
            }
        });
    }

    @Override
    public void onCategorySelected(@Nullable Measurement.Category category) {
        addMeasurementView(category);
    }

    @Override
    public void onMiscellaneousSelected() {
        showDialogCategories();
    }

    @Override
    public void onCategoryAdded(Measurement.Category category) {
        fab.ignore(category);
        fab.restock();
    }

    @Override
    public void onCategoryRemoved(Measurement.Category category) {
        fab.removeIgnore(category);
        fab.restock();
    }

    private class FetchEntryTask extends AsyncTask<Void, Void, Entry> {

        private long entryId;

        private FetchEntryTask(long entryId) {
            this.entryId = entryId;
        }

        @Override
        protected Entry doInBackground(Void... params) {
            EntryDao dao = EntryDao.getInstance();
            Entry entry = dao.get(entryId);
            if (entry != null) {
                entry.setMeasurementCache(dao.getMeasurements(entry));
            }
            return entry;
        }

        @Override
        protected void onPostExecute(Entry entry) {
            super.onPostExecute(entry);
            initEntry(entry);
        }
    }

    private class FetchFoodTask extends AsyncTask<Void, Void, Food> {

        private long foodId;

        private FetchFoodTask(long foodId) {
            this.foodId = foodId;
        }

        @Override
        protected Food doInBackground(Void... params) {
            return FoodDao.getInstance().get(foodId);
        }

        @Override
        protected void onPostExecute(Food food) {
            super.onPostExecute(food);
            initFood(food);
        }
    }
}