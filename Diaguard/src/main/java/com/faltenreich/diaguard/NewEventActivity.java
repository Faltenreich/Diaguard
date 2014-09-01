package com.faltenreich.diaguard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Food;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.database.Model;
import com.faltenreich.diaguard.fragments.DatePickerFragment;
import com.faltenreich.diaguard.fragments.TimePickerFragment;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.Validator;
import com.faltenreich.diaguard.helpers.ViewHelper;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;

/**
 * Created by Filip on 19.10.13.
 */
public class NewEventActivity extends ActionBarActivity {

    public static final String EXTRA_ENTRY = "Entry";
    public static final String EXTRA_MEASUREMENT = "Measurement";
    public static final String EXTRA_DATE = "Date";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    String currentPhotoPath;

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;

    DateTime time;
    boolean inputWasMade;
    boolean mealInfoIsVisible;

    LinearLayout linearLayoutValues;
    EditText editTextNotes;
    Button buttonDate;
    Button buttonTime;
    ImageView imageViewCamera;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newevent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.newevent));
        initialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
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
        inputWasMade = false;

        getComponents();
        checkIntents();
        setDate();
        setTime();
        setCategories();
    }

    public void getComponents() {
        linearLayoutValues = (LinearLayout) findViewById(R.id.content_dynamic);
        editTextNotes = (EditText) findViewById(R.id.edittext_notes);
        buttonDate = (Button) findViewById(R.id.button_date);
        buttonTime = (Button) findViewById(R.id.button_time);
        imageViewCamera = (ImageView) findViewById(R.id.button_camera);
    }

    private void checkIntents() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Get entry and all of its measurements
            if(extras.getLong(EXTRA_ENTRY) != 0L || extras.getLong(EXTRA_MEASUREMENT) != 0L) {
                dataSource.open();
                Entry entry;
                if(extras.getLong(EXTRA_ENTRY) != 0L) {
                    entry = (Entry) dataSource.get(DatabaseHelper.ENTRY, extras.getLong(EXTRA_ENTRY));
                }
                else {
                    Measurement measurement = (Measurement)dataSource.get(DatabaseHelper.MEASUREMENT, extras.getLong("ID"));
                    entry = (Entry)dataSource.get(DatabaseHelper.ENTRY, measurement.getEntryId());
                }
                List<Model> measurements = dataSource.get(DatabaseHelper.MEASUREMENT, null,
                        DatabaseHelper.ENTRY_ID + "=?", new String[]{Long.toString(entry.getId())},
                        null, null, null, null);
                dataSource.close();

                time = entry.getDate();
                editTextNotes.setText(entry.getNote());

                for(Model model : measurements) {
                    Measurement measurement = (Measurement) model;
                    entry.getMeasurements().add(measurement);
                    addValue(measurement.getCategory(), preferenceHelper.getDecimalFormat(
                            measurement.getCategory()).format(measurement.getValue()));
                }
            }
            else if(extras.getSerializable(EXTRA_DATE) != null) {
                time = (DateTime) extras.getSerializable(EXTRA_DATE);
            }
        }
    }

    private void setDate() {
        buttonDate.setText(preferenceHelper.getDateFormat().print(time));
    }

    private void setTime() {
        buttonTime.setText(Helper.getTimeFormat().print(time));
    }

    private void setCategories() {
        for(Measurement.Category category : preferenceHelper.getActiveCategories())
            addValue(category, null);
    }

    private void submit() {
        Food food = null;
        boolean inputIsValid = true;

        // Validate date
        DateTime now = new DateTime();
        if (time.isAfter(now)) {
            ViewHelper.showAlert(this, getString(R.string.validator_value_infuture));
            return;
        }

        List<Measurement> measurements = new ArrayList<Measurement>();
        // Iterate through all views and validate
        for (int position = 0; position < linearLayoutValues.getChildCount(); position++) {
            View view = linearLayoutValues.getChildAt(position);
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

                else if(view.getTag() instanceof String) {
                    String tag = (String)view.getTag();
                    if(tag.equals(DatabaseHelper.FOOD)) {
                        AutoCompleteTextView editTextFood = (AutoCompleteTextView) view.findViewById(R.id.food);

                        // Check if a Meal has been entered and get its values
                        boolean mealIsAvailable = false;
                        int eventPosition = 0;
                        while(!mealIsAvailable && eventPosition < measurements.size()) {
                            if(measurements.get(eventPosition).getCategory() == Measurement.Category.Meal)
                                mealIsAvailable = true;
                            eventPosition++;
                        }

                        if(mealIsAvailable) {
                            food = new Food();
                            // TODO handle position better
                            food.setCarbohydrates(measurements.get(eventPosition-1).getValue());
                            food.setName(editTextFood.getText().toString());
                            food.setDate(time);
                            // eventId is set later
                        }
                    }
                }
            }
        }

        // Check whether there are values to submit
        if(measurements.size() == 0) {
            ViewHelper.showAlert(this, getString(R.string.validator_value_none));
            inputIsValid = false;
        }

        if(inputIsValid) {
            dataSource.open();

            // Entry
            Entry entry = new Entry();
            entry.setDate(time);
            if(editTextNotes.length() > 0)
                entry.setNote(editTextNotes.getText().toString());
            long entryId = dataSource.insert(entry);
            // Connect measurements with entry
            for(Measurement measurement : measurements)
                measurement.setEntryId(entryId);

            // Events
            long[] ids;
            /*
            TODO
            Bundle extras = getIntent().getExtras();
            // Update existing
            if (extras != null && extras.getLong(EXTRA_ENTRY) != 0L) {
                measurements.get(0).setId(extras.getLong(EXTRA_ENTRY));
                ids = new long[1];
                ids[0] = dataSource.update(measurements.get(0));
            }
            // Insert new
            else {
                ids = dataSource.insert(measurements);
            }
            */

            ids = dataSource.insert(measurements);

            // Food
            if(food != null) {
                for(int position = 0; position < measurements.size(); position++) {
                    if(measurements.get(position).getCategory() == Measurement.Category.Meal) {
                        food.setMeasurementId(ids[position]);
                        dataSource.insert(food);
                    }
                }
            }

            dataSource.close();

            // Tell MainActivity that Events have been created
            Intent intent = new Intent();
            intent.putExtra(MainActivity.EVENT_CREATED, measurements.size());
            setResult(Activity.RESULT_OK, intent);

            finish();
        }
    }

    private void addValue(final Measurement.Category category, String value) {
        // Add view
        final LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_newvalue, linearLayoutValues, false);
        view.setTag(category);

        // Category name
        TextView textViewCategory = (TextView) view.findViewById(R.id.category);
        textViewCategory.setText(preferenceHelper.getCategoryName(category));

        // Status image
        final View viewStatus = view.findViewById(R.id.status);

        // Value
        final EditText editTextValue = (EditText) view.findViewById(R.id.value);
        editTextValue.setHint(preferenceHelper.getUnitAcronym(category));
        if(value != null) {
            editTextValue.setText(value);
        }
        if(category == Measurement.Category.BloodSugar)
            editTextValue.requestFocus();

        // OnChangeListener
        TextWatcher textChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputWasMade = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(editTextValue.getText().length() == 0) {
                    viewStatus.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                else {
                    if(!preferenceHelper.validateEventValue(
                            category, preferenceHelper.formatCustomToDefaultUnit(category,
                                    Float.parseFloat(editTextValue.getText().toString())))) {
                        viewStatus.setBackgroundColor(getResources().getColor(R.color.red));
                    }
                    else
                        viewStatus.setBackgroundColor(getResources().getColor(R.color.green));

                    // Show an additional View for food information
                    if(category == Measurement.Category.Meal && !mealInfoIsVisible) {
                        View viewMealInfo = inflater.inflate(R.layout.fragment_meal_info, linearLayoutValues, false);
                        viewMealInfo.setTag(DatabaseHelper.FOOD);
                        linearLayoutValues.addView(viewMealInfo, 3);

                        // AutoComplete
                        dataSource.open();
                        List<Model> foodList = dataSource.get(DatabaseHelper.FOOD, null, null, null, null, null, null, null);
                        dataSource.close();
                        String[] foodNames = new String[foodList.size()];
                        for(int foodPosition = 0; foodPosition < foodList.size(); foodPosition++) {
                            Food food = (Food)foodList.get(foodPosition);
                            foodNames[foodPosition] = food.getName();
                        }
                        AutoCompleteTextView editTextFood = (AutoCompleteTextView)viewMealInfo.findViewById(R.id.food);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewEventActivity.this, android.R.layout.simple_dropdown_item_1line, foodNames);
                        editTextFood.setAdapter(adapter);

                        ViewHelper.expand(viewMealInfo);
                        mealInfoIsVisible = true;
                    }
                }
            }
        };
        editTextValue.addTextChangedListener(textChangedListener);

        linearLayoutValues.addView(view, linearLayoutValues.getChildCount());
    }

    private void deleteEvent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long id = extras.getLong("ID");
            if (id != 0L) {
                dataSource.open();
                //Event event = dataSource.getEventById(id);
                //dataSource.deleteEvent(event);
                dataSource.close();
                finish();
            }
        }
    }

    // LISTENERS

    public void onClickShowDatePicker (View view) {
        DialogFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time = time.withYear(year).withMonthOfYear(month).withDayOfMonth(day);
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
    public void onBackPressed() {
        if(inputWasMade) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.confirmation_exit))
                    .setMessage(getString(R.string.confirmation_exit_desc))
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        }
        else
            finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_delete:
                deleteEvent();
                return true;
            case R.id.action_done:
                submit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}