package com.faltenreich.diaguard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.database.Model;
import com.faltenreich.diaguard.fragments.DatePickerFragment;
import com.faltenreich.diaguard.fragments.TimePickerFragment;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.Validator;
import com.faltenreich.diaguard.helpers.ViewHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 19.10.13.
 */
public class NewEventActivity extends ActionBarActivity {

    public static final String EXTRA_ENTRY = "Entry";
    public static final String EXTRA_MEASUREMENT = "Measurement";
    public static final String EXTRA_DATE = "Date";

    private DatabaseDataSource dataSource;
    private PreferenceHelper preferenceHelper;

    private Entry entry;
    private int alarmIntervalInMinutes;

    private DateTime time;

    private FloatingActionMenu floatingActionMenu;
    private LinearLayout layoutValues;
    private EditText editTextNotes;
    private Button buttonDate;
    private Button buttonTime;
    private Spinner spinnerAlarm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newevent);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.formular, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void initialize() {
        dataSource = new DatabaseDataSource(this);
        preferenceHelper = new PreferenceHelper(this);
        time = new DateTime();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setTitle(getString(R.string.entry_new));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            setSupportActionBar(toolbar);
        }

        getComponents();
        setCategories();
        checkIntents();
        setDate();
        setTime();
        setFloatingActionMenu();

        alarmIntervalInMinutes = 0;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarm_intervals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlarm.setAdapter(adapter);
        spinnerAlarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                alarmIntervalInMinutes = getResources().getIntArray(R.array.alarm_intervals_values)[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void getComponents() {
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        layoutValues = (LinearLayout) findViewById(R.id.layout_measurements);
        editTextNotes = (EditText) findViewById(R.id.edittext_notes);
        buttonDate = (Button) findViewById(R.id.button_date);
        buttonTime = (Button) findViewById(R.id.button_time);
        spinnerAlarm = (Spinner) findViewById(R.id.spinner_alarm);
    }

    private void checkIntents() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getLong(EXTRA_ENTRY) != 0L || extras.getLong(EXTRA_MEASUREMENT) != 0L) {

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                if (toolbar != null) {
                    toolbar.setTitle(getString(R.string.entry_edit));
                }

                dataSource.open();

                // Get entry
                if(extras.getLong(EXTRA_ENTRY) != 0L) {
                    entry = (Entry) dataSource.get(DatabaseHelper.ENTRY, extras.getLong(EXTRA_ENTRY));
                }
                else {
                    Measurement measurement = (Measurement)dataSource.get(DatabaseHelper.MEASUREMENT, extras.getLong("ID"));
                    entry = (Entry)dataSource.get(DatabaseHelper.ENTRY, measurement.getEntryId());
                }

                // and all of its measurements
                List<Model> measurements = dataSource.get(DatabaseHelper.MEASUREMENT, null,
                        DatabaseHelper.ENTRY_ID + "=?", new String[]{ Long.toString(entry.getId()) },
                        null, null, null, null);
                dataSource.close();

                time = entry.getDate();
                editTextNotes.setText(entry.getNote());

                for(Model model : measurements) {
                    Measurement measurement = (Measurement) model;
                    // TODO entry.getMeasurements().add(measurement);
                    for(int position = 0; position < layoutValues.getChildCount(); position++) {
                        View view = layoutValues.getChildAt(position);
                        Measurement.Category category = (Measurement.Category)view.getTag();
                        if(category == measurement.getCategory()) {
                            EditText editTextValue = (EditText) view.findViewById(R.id.value);
                            float customValue = preferenceHelper.formatDefaultToCustomUnit(category, measurement.getValue());
                            editTextValue.setText(Helper.getDecimalFormat().format(customValue));
                        }
                    }
                }
            }
            else if(extras.getSerializable(EXTRA_DATE) != null) {
                time = (DateTime) extras.getSerializable(EXTRA_DATE);
            }
        }
    }

    private void setFloatingActionMenu() {
        Measurement.Category[] mostUsedCategories = new Measurement.Category[] {
                Measurement.Category.BloodSugar,
                Measurement.Category.Bolus,
                Measurement.Category.Meal
        };
        for(Measurement.Category category : mostUsedCategories) {
            addFloatingActionButton(preferenceHelper.getCategoryName(category),
                    getResources().getIdentifier(category.name().toLowerCase() + "_white", "drawable", getPackageName()),
                    R.color.green, R.color.green_lt, R.color.green_dk);
        }
        addFloatingActionButton(getString(R.string.all), R.drawable.ic_other,
                android.R.color.white, R.color.gray_light, R.color.gray_light);
        // Hide menu on click outside
        floatingActionMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    if (floatingActionMenu.isOpened()) {
                        floatingActionMenu.close(true);
                    }
                }
                return true;
            }
        });
    }

    private void addFloatingActionButton(String text, int imageResourceId,
                                         int colorNormalResId, int colorPressedResId, int colorRippleResId) {
        FloatingActionButton floatingActionButton = new FloatingActionButton(this);
        floatingActionButton.setButtonSize(FloatingActionButton.SIZE_MINI);
        floatingActionButton.setLabelText(text);
        floatingActionButton.setImageResource(imageResourceId);
        floatingActionButton.setColorNormalResId(colorNormalResId);
        floatingActionButton.setColorPressedResId(colorPressedResId);
        floatingActionButton.setColorRippleResId(colorRippleResId);
        floatingActionMenu.addMenuButton(floatingActionButton);
    }

    private void setDate() {
        buttonDate.setText(preferenceHelper.getDateFormat().print(time));
    }

    private void setTime() {
        buttonTime.setText(Helper.getTimeFormat().print(time));
    }

    private void setCategories() {
        for(Measurement.Category category : preferenceHelper.getActiveCategories())
            addValue(category);
    }

    private void submit() {
        boolean inputIsValid = true;

        // Validate date
        DateTime now = new DateTime();
        if (time.isAfter(now)) {
            ViewHelper.showAlert(this, getString(R.string.validator_value_infuture));
            return;
        }

        List<Measurement> measurements = new ArrayList<Measurement>();
        // Iterate through all views and validate
        for (int position = 0; position < layoutValues.getChildCount(); position++) {
            View view = layoutValues.getChildAt(position);
            if(view != null && view.getTag() != null) {
                if(view.getTag() instanceof Measurement.Category) {
                    EditText editTextValue = (EditText) view.findViewById(R.id.value);
                    String editTextText = editTextValue.getText().toString();

                    if(editTextText.length() > 0) {
                        Measurement.Category category = (Measurement.Category) view.getTag();

                        if (!Validator.containsNumber(editTextText)) {
                            editTextValue.setError(getString(R.string.validator_value_empty));
                            inputIsValid = false;
                        }
                        else if (!preferenceHelper.validateEventValue(
                                category, preferenceHelper.formatCustomToDefaultUnit(category,
                                        Float.parseFloat(editTextText)))) {
                            editTextValue.setError(getString(R.string.validator_value_unrealistic));
                            inputIsValid = false;
                        }
                        else {
                            editTextValue.setError(null);
                            Measurement measurement = new Measurement();
                            float value = preferenceHelper.formatCustomToDefaultUnit(category, Float.parseFloat(editTextText));
                            measurement.setValue(value);
                            measurement.setCategory(category);
                            measurements.add(measurement);
                        }
                    }
                }
            }
        }

        // Check whether there are values to submit
        if(measurements.size() == 0) {
            // Show alert only if everything else was valid to reduce clutter
            if(inputIsValid)
                ViewHelper.showAlert(this, getString(R.string.validator_value_none));
            inputIsValid = false;
        }

        if(inputIsValid) {
            dataSource.open();

            // Update existing entry
            if(entry != null) {
                entry.setDate(time);
                entry.setNote(editTextNotes.getText().toString());
                dataSource.update(entry);

                // TODO
                /*
                // Step through measurements and compare
                List<Measurement> measurementsToDelete = new ArrayList<Measurement>(entry.getMeasurements());
                for(Measurement measurement : measurements) {
                    // Case 1: Measurement is new and old --> Update
                    boolean updatedExistingMeasurement = false;
                    for(Measurement oldMeasurement : entry.getMeasurements()) {
                        if (measurement.getCategory() == oldMeasurement.getCategory()) {
                            oldMeasurement.setValue(measurement.getValue());
                            updatedExistingMeasurement = true;
                            measurementsToDelete.remove(oldMeasurement);
                            dataSource.update(oldMeasurement);
                        }
                    }
                    // Case 2: Measurement is new but not old --> Insert
                    if(!updatedExistingMeasurement) {
                        measurement.setEntryId(entry.getId());
                        dataSource.insert(measurement);
                    }
                }
                // Case 3: Measurement is old but not new --> Delete
                for(Measurement measurement : measurementsToDelete) {
                    dataSource.delete(measurement);
                }
                    */
            }

            // Insert new entry
            else {
                entry = new Entry();
                entry.setDate(time);
                if(editTextNotes.length() > 0)
                    entry.setNote(editTextNotes.getText().toString());
                long entryId = dataSource.insert(entry);

                // Connect measurements with entry
                for(Measurement measurement : measurements) {
                    measurement.setEntryId(entryId);
                    dataSource.insert(measurement);
                }
            }
            dataSource.close();

            if(alarmIntervalInMinutes > 0) {
                Helper.setAlarm(this, alarmIntervalInMinutes);
            }

            // Tell MainActivity that Events have been created
            Intent intent = new Intent();
            intent.putExtra(MainActivity.ENTRY_CREATED, measurements.size());
            setResult(Activity.RESULT_OK, intent);

            finish();
        }
    }

    private void addValue(final Measurement.Category category) {
        // Add view
        final LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_newvalue, layoutValues, false);
        view.setTag(category);

        // Category name
        TextView textViewCategory = (TextView) view.findViewById(R.id.category);
        textViewCategory.setText(preferenceHelper.getCategoryName(category));

        // Value
        EditText editTextValue = (EditText) view.findViewById(R.id.value);
        editTextValue.setHint(preferenceHelper.getUnitAcronym(category));
        if(category == Measurement.Category.BloodSugar) {
            editTextValue.requestFocus();
        }

        layoutValues.addView(view, layoutValues.getChildCount());
    }

    // LISTENERS

    public void onClickShowDatePicker (View view) {
        DialogFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time = time.withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day);
                setDate();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, time);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "DatePicker");
    }

    public void onClickShowTimePicker (View view) {
        DialogFragment fragment = new TimePickerFragment() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = time.withHourOfDay(hourOfDay).withMinuteOfHour(minute);
                setTime();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(TimePickerFragment.TIME, time);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "TimePicker");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_done:
                submit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}