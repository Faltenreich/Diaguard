package com.android.diaguard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.android.diaguard.fragments.DatePickerFragment;
import com.android.diaguard.fragments.TimePickerFragment;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;
import com.android.diaguard.helpers.SwipeDismissTouchListener;
import com.android.diaguard.helpers.Validator;
import com.android.diaguard.helpers.ViewHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Filip on 19.10.13.
 */
public class NewEventActivity extends ActionBarActivity {

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;

    Calendar time;
    String[] activeCategories;
    boolean[] selectedCategories;

    LinearLayout linearLayoutValues;
    EditText editTextNotes;
    Button buttonDate;
    Button buttonTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newevent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.newevent));
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.formular, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void initialize() {

        dataSource = new DatabaseDataSource(this);
        preferenceHelper = new PreferenceHelper(this);

        time = Calendar.getInstance();
        activeCategories = preferenceHelper.getActiveCategoriesNames();
        selectedCategories = new boolean[activeCategories.length];

        getComponents();
        initializeGUI();
        checkIntents();

        setDate();
        setTime();
    }

    public void getComponents() {
        linearLayoutValues = (LinearLayout) findViewById(R.id.content_dynamic);
        editTextNotes = (EditText) findViewById(R.id.edittext_notes);
        buttonDate = (Button) findViewById(R.id.button_date);
        buttonTime = (Button) findViewById(R.id.button_time);
    }

    public void initializeGUI() {}

    private void checkIntents() {

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            if(extras.getSerializable("Date") != null)
                time = (Calendar)extras.getSerializable("Date");

            if (extras.getLong("ID") != 0L) {
                setTitle(getString(R.string.editevent));

                dataSource.open();
                Event event = dataSource.getEventById(extras.getLong("ID"));
                dataSource.close();

                time = event.getDate();

                float value = preferenceHelper.
                        formatDefaultToCustomUnit(event.getCategory(), event.getValue());
                addValue(event.getCategory(), value);

                editTextNotes.setText(event.getNotes());

                findViewById(R.id.button_add).setVisibility(View.GONE);
            }
            else
                findViewById(R.id.button_add).setVisibility(View.VISIBLE);
        }
    }

    private void setDate() {
        SimpleDateFormat format = preferenceHelper.getDateFormat();
        buttonDate.setText(format.format(time.getTime()));
    }

    private void setTime() {
        SimpleDateFormat format = preferenceHelper.getTimeFormat();
        buttonTime.setText(format.format(time.getTime()));
    }

    private void submit() {

        // Iterate through all generated views
        int valueCount = linearLayoutValues.getChildCount();
        if(valueCount == 0) {
            ViewHelper.showToastError(this, getString(R.string.validator_value_none));
        }
        else {
            List<Event> events = new ArrayList<Event>();
            boolean inputIsValid = true;

            for (int position = 0; position < linearLayoutValues.getChildCount(); position++) {

                View view = linearLayoutValues.getChildAt(position);
                EditText editTextValue = (EditText) view.findViewById(R.id.value);
                String editTextText = editTextValue.getText().toString();
                Event.Category category = (Event.Category) view.getTag();

                // Validation
                Calendar now = Calendar.getInstance();
                if(time.after(now)) {
                    ViewHelper.showToastError(this, getString(R.string.validator_value_infuture));
                    inputIsValid = false;
                }

                if(!Validator.containsNumber(editTextText)) {
                    editTextValue.setError(getString(R.string.validator_value_empty));
                    inputIsValid = false;
                }
                else if(!Validator.validateEventValue(this, category, preferenceHelper.formatCustomToDefaultUnit(category, Float.parseFloat(editTextText)))) {
                    editTextValue.setError(getString(R.string.validator_value_unrealistic));
                    inputIsValid = false;
                }
                else
                    editTextValue.setError(null);

                if(inputIsValid) {
                    Event event = new Event();
                    float value = preferenceHelper.formatCustomToDefaultUnit(category, Float.parseFloat(editTextText));
                    event.setValue(value);
                    event.setDate(time);
                    event.setNotes(editTextNotes.getText().toString());
                    event.setCategory(category);
                    events.add(event);
                }
            }

            if(inputIsValid) {
                dataSource.open();
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    long id = extras.getLong("ID");
                    if (id != 0L) {
                        events.get(0).setId(id);
                        dataSource.updateEvent(events.get(0));
                    }
                    else
                        dataSource.insertEvents(events);
                }
                else {
                    dataSource.insertEvents(events);
                }
                dataSource.close();
                finish();
            }
        }
    }

    private void addValue(Event.Category category) {

        // Add view
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_newvalue, null);
        view.setTag(category);

        view.setOnTouchListener(new SwipeDismissTouchListener(view, null,
            new SwipeDismissTouchListener.OnDismissCallback() {
                @Override
                public void onDismiss(View view1, Object token) {
                    int position = Arrays.asList(preferenceHelper.getActiveCategories()).indexOf(view.getTag());
                    if(position != -1)
                        selectedCategories[position] = false;
                    linearLayoutValues.removeView(view);
                }
            }
        ));
        // Must be overwritten for SwipeToDismiss to work properly (WTF?)
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        ImageView image = (ImageView) view.findViewById(R.id.image);
        String name = category.name().toLowerCase();
        int resourceId = getResources().getIdentifier(name,
                "drawable", getPackageName());
        image.setImageResource(resourceId);

        TextView categoryName = (TextView) view.findViewById(R.id.category);
        categoryName.setText(preferenceHelper.getCategoryName(category));

        TextView unit = (TextView) view.findViewById(R.id.unit);
        unit.setText(preferenceHelper.getUnitAcronym(category));

        Animation animationSlideInLeft =
                AnimationUtils.loadAnimation(NewEventActivity.this,
                        android.R.anim.slide_in_left);
        animationSlideInLeft.setDuration(300);
        view.startAnimation(animationSlideInLeft);

        linearLayoutValues.addView(view, 0);
    }

    private void addValue(Event.Category category, float value) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_newvalue, null);
        view.setTag(category);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        String name = category.name().toLowerCase();
        int resourceId = getResources().getIdentifier(name,
                "drawable", getPackageName());
        image.setImageResource(resourceId);

        TextView textViewCategoryName = (TextView) view.findViewById(R.id.category);
        textViewCategoryName.setText(preferenceHelper.getCategoryName(category));

        TextView textViewUnit = (TextView) view.findViewById(R.id.unit);
        textViewUnit.setText(preferenceHelper.getUnitAcronym(category));

        EditText editTextValue = (EditText) view.findViewById(R.id.value);
        editTextValue.setText(Helper.getDecimalFormat().format(value));
        editTextValue.requestFocus();
        editTextValue.setSelection(editTextValue.getText().length());

        linearLayoutValues.addView(view);
    }

    // LISTENERS

    public void onClickShowDatePicker (View view) {

        DialogFragment newFragment = new DatePickerFragment(time) {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time.set(Calendar.YEAR, year);
                time.set(Calendar.MONTH, month);
                time.set(Calendar.DAY_OF_MONTH, day);
                setDate();
            }
        };
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    public void onClickShowTimePicker (View view) {

        DialogFragment newFragment = new TimePickerFragment(time) {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);
                setTime();
            }
        };
        newFragment.show(getSupportFragmentManager(), "TimePicker");
    }

    public void onClickAddValue (View view) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewEventActivity.this);
        dialogBuilder.setMultiChoiceItems(activeCategories, selectedCategories,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (int position = selectedCategories.length-1; position >= 0; position--)
                            if (selectedCategories[position])
                                addValue(Event.Category.values()[position]);
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
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

