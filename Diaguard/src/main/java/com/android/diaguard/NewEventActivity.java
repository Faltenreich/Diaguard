package com.android.diaguard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TimePicker;

import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.android.diaguard.fragments.DatePickerFragment;
import com.android.diaguard.fragments.TimePickerFragment;
import com.android.diaguard.helpers.PreferenceHelper;
import com.android.diaguard.adapters.SwipeDismissTouchListener;
import com.android.diaguard.helpers.Validator;
import com.android.diaguard.helpers.ViewHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Filip on 19.10.13.
 */
public class NewEventActivity extends ActionBarActivity {

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_DATE = "Date";

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;

    Calendar time;
    LinkedHashMap<Event.Category, Boolean> selectedCategoriesMap;
    boolean inputWasMade;

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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem deleteEvent = menu.findItem(R.id.action_delete);
        if(deleteEvent != null) {
            deleteEvent.setVisible(false);
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.getLong(EXTRA_ID) != 0L)
                deleteEvent.setVisible(true);
        }

        return true;
    }

    public void initialize() {
        dataSource = new DatabaseDataSource(this);
        preferenceHelper = new PreferenceHelper(this);

        time = Calendar.getInstance();
        inputWasMade = false;

        selectedCategoriesMap = new LinkedHashMap<Event.Category, Boolean>();
        for (Event.Category category : preferenceHelper.getActiveCategories()) {
            selectedCategoriesMap.put(category, false);
        }

        getComponents();
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

    private void checkIntents() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getLong(EXTRA_ID) != 0L) {
                findViewById(R.id.layout_newvalue).setVisibility(View.GONE);

                dataSource.open();
                Event event = dataSource.getEventById(extras.getLong("ID"));
                dataSource.close();

                time = event.getDate();
                float value = preferenceHelper.
                        formatDefaultToCustomUnit(event.getCategory(), event.getValue());
                editTextNotes.setText(event.getNotes());
                addValue(event.getCategory(), preferenceHelper.getDecimalFormat(
                        event.getCategory()).format(value), false, false);
            }

            else if(extras.getSerializable(EXTRA_DATE) != null) {
                time = (Calendar) extras.getSerializable(EXTRA_DATE);
            }
        }
    }

    private void setDate() {
        buttonDate.setText(preferenceHelper.getDateFormat().format(time.getTime()));
    }

    private void setTime() {
        buttonTime.setText(preferenceHelper.getTimeFormat().format(time.getTime()));
    }

    private void submit() {
        // Check whether there are values to submit
        if(linearLayoutValues.getChildCount() == 0) {
            ViewHelper.showToastError(this, getString(R.string.validator_value_none));
            return;
        }

        List<Event> events = new ArrayList<Event>();
        boolean inputIsValid = true;

        // Iterate through all generated views
        for (int position = 0; position < linearLayoutValues.getChildCount(); position++) {
            View view = linearLayoutValues.getChildAt(position);

            if(view != null) {
                EditText editTextValue = (EditText) view.findViewById(R.id.value);
                String editTextText = editTextValue.getText().toString();
                Event.Category category = (Event.Category) view.getTag();

                // Validation
                Calendar now = Calendar.getInstance();
                if (time.after(now)) {
                    ViewHelper.showToastError(this, getString(R.string.validator_value_infuture));
                    inputIsValid = false;
                }

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
                }

                if (inputIsValid) {
                    Event event = new Event();
                    float value = preferenceHelper.formatCustomToDefaultUnit(category, Float.parseFloat(editTextText));
                    event.setValue(value);
                    event.setDate(time);
                    event.setNotes(editTextNotes.getText().toString());
                    event.setCategory(category);
                    events.add(event);
                }
            }
        }

        if(inputIsValid) {
            dataSource.open();
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.getLong(EXTRA_ID) != 0L) {
                events.get(0).setId(extras.getLong(EXTRA_ID));
                dataSource.updateEvent(events.get(0));
            }
            else {
                dataSource.insertEvents(events);
            }
            dataSource.close();
            finish();
        }
    }

    private void addValue(final Event.Category category, String value, boolean animate, boolean removable) {
        selectedCategoriesMap.put(category, true);

        // Add view
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_newvalue, linearLayoutValues, false);
        view.setTag(category);

        // Swipe to dismiss
        if(removable) {
            view.setOnTouchListener(new SwipeDismissTouchListener(view, null,
                    new SwipeDismissTouchListener.OnDismissCallback() {
                        @Override
                        public void onDismiss(View view1, Object token) {
                            removeValue(category);
                        }
                    }
            ));
            // Must be overwritten for SwipeToDismiss to work properly (WTF?)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        // Category image
        ImageView image = (ImageView) view.findViewById(R.id.image);
        String name = category.name().toLowerCase();
        int resourceId = getResources().getIdentifier(name,
                "drawable", getPackageName());
        image.setImageResource(resourceId);

        // Value
        EditText editTextValue = (EditText) view.findViewById(R.id.value);
        editTextValue.setHint(preferenceHelper.getUnitAcronym(category));
        if(value != null) {
            editTextValue.setText(value);
        }

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
            }
        };
        editTextValue.addTextChangedListener(textChangedListener);

        // Removable
        ImageView imageDelete = (ImageView) view.findViewById(R.id.delete);
        if(removable)
            imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeValue(category);
            }
        });
        else
            imageDelete.setVisibility(View.GONE);

        // Animation
        if(animate) {
            Animation animationSlideInLeft =
                    AnimationUtils.loadAnimation(NewEventActivity.this, android.R.anim.slide_in_left);
            animationSlideInLeft.setDuration(300);
            view.startAnimation(animationSlideInLeft);
        }

        linearLayoutValues.addView(view, 0);
    }

    private void removeValue(Event.Category category) {
        selectedCategoriesMap.put(category, false);

        for(int position = 0; position < linearLayoutValues.getChildCount(); position++) {
            View view = linearLayoutValues.getChildAt(position);

            if(view != null && view.getTag() != null && view.getTag() instanceof Event.Category) {
                Event.Category childCategory = (Event.Category) view.getTag();
                if(category == childCategory) {
                    linearLayoutValues.removeViewAt(position);
                    if(linearLayoutValues.getChildCount() == 0)
                        inputWasMade = false;
                }
            }
        }
    }

    private void deleteEvent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long id = extras.getLong("ID");
            if (id != 0L) {
                dataSource.open();
                Event event = dataSource.getEventById(id);
                dataSource.deleteEvent(event);
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
                time.set(Calendar.YEAR, year);
                time.set(Calendar.MONTH, month);
                time.set(Calendar.DAY_OF_MONTH, day);
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
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);
                setTime();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(TimePickerFragment.TIME, time);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "TimePicker");
    }

    public void onClickAddValue (View view) {
        String[] categoryNames = new String[selectedCategoriesMap.size()];
        final boolean[] selectedCategories = new boolean[selectedCategoriesMap.size()];

        int position = 0;
        for (Map.Entry entry : selectedCategoriesMap.entrySet()) {
            categoryNames[position] = preferenceHelper.getCategoryName((Event.Category)entry.getKey());
            selectedCategories[position] = (Boolean)entry.getValue();
            position++;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewEventActivity.this);
        dialogBuilder.setMultiChoiceItems(categoryNames, selectedCategories,
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
                        List<Boolean> selectedCategoriesList = new ArrayList<Boolean>(selectedCategoriesMap.values());

                        for (int position = selectedCategories.length - 1; position >= 0; position--) {
                            boolean categoryWasSelectedBefore = selectedCategoriesList.get(position);

                            // Add new value
                            if (selectedCategories[position] && !categoryWasSelectedBefore)
                                addValue(Event.Category.values()[position], null, true, true);

                            // Remove old value
                            else if (!selectedCategories[position] && categoryWasSelectedBefore)
                                removeValue(Event.Category.values()[position]);
                        }
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
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