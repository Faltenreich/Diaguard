package com.faltenreich.diaguard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.Event;
import com.faltenreich.diaguard.fragments.DatePickerFragment;
import com.faltenreich.diaguard.fragments.TimePickerFragment;
import com.faltenreich.diaguard.helpers.FileHelper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.Validator;
import com.faltenreich.diaguard.helpers.ViewHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Filip on 19.10.13.
 */
public class NewEventActivity extends ActionBarActivity {

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_DATE = "Date";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    String currentPhotoPath;

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;

    Calendar time;
    boolean inputWasMade;
    Bitmap imageTemp;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.formular, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item, menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageTemp = (Bitmap) extras.get("data");
            imageViewCamera.setImageBitmap(imageTemp);
            imageViewCamera.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    public void initialize() {
        dataSource = new DatabaseDataSource(this);
        preferenceHelper = new PreferenceHelper(this);
        time = Calendar.getInstance();
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
            if (extras.getLong(EXTRA_ID) != 0L) {
                dataSource.open();
                Event event = dataSource.getEventById(extras.getLong("ID"));
                dataSource.close();

                time = event.getDate();
                float value = preferenceHelper.
                        formatDefaultToCustomUnit(event.getCategory(), event.getValue());
                editTextNotes.setText(event.getNotes());
                addValue(event.getCategory(), preferenceHelper.getDecimalFormat(
                        event.getCategory()).format(value));
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

    private void setCategories() {
        Event.Category[] activeCategories = preferenceHelper.getActiveCategories();
        for(int categoryPosition = activeCategories.length - 1; categoryPosition >= 0; categoryPosition--) {
            addValue(activeCategories[categoryPosition], null);
        }
    }

    private void submit() {
        List<Event> events = new ArrayList<Event>();
        boolean inputIsValid = true;

        // Validate date
        Calendar now = Calendar.getInstance();
        if (time.after(now)) {
            ViewHelper.showToastError(this, getString(R.string.validator_value_infuture));
            inputIsValid = false;
        }

        // Iterate through all views and validate
        for (int position = 0; position < linearLayoutValues.getChildCount(); position++) {
            View view = linearLayoutValues.getChildAt(position);
            if(view != null) {
                EditText editTextValue = (EditText) view.findViewById(R.id.value);
                String editTextText = editTextValue.getText().toString();

                if(editTextText.length() > 0) {
                    Event.Category category = (Event.Category) view.getTag();

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
        }

        // Check whether there are values to submit
        if(events.size() == 0) {
            ViewHelper.showToastError(this, getString(R.string.validator_value_none));
            inputIsValid = false;
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

    private void addValue(final Event.Category category, String value) {
        // Add view
        LayoutInflater inflater = getLayoutInflater();
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
        if(category == Event.Category.BloodSugar)
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
                }
            }
        };
        editTextValue.addTextChangedListener(textChangedListener);

        linearLayoutValues.addView(view, 0);
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "test";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
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

    public void onClickCamera(View view) {
        // Check if an image has already been shot
        if(imageTemp == null) {
            // Open camera app
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = Uri.parse(FileHelper.PATH_STORAGE + "/image");
            if (intent.resolveActivity(getPackageManager()) != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
        else {
            // Open context menu
            registerForContextMenu(imageViewCamera);
            openContextMenu(imageViewCamera);
            unregisterForContextMenu(imageViewCamera);
        }
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + "/sdcard/test.jpg"), "image/*");
                startActivity(intent);
                return true;
            case R.id.remove:
                imageViewCamera.setImageDrawable(getResources().getDrawable(R.drawable.camera));
                imageViewCamera.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageTemp = null;
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}