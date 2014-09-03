package com.faltenreich.diaguard;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.database.Model;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;

import java.util.HashMap;
import java.util.List;


public class EntryActivity extends ActionBarActivity {

    public static final String EXTRA_ENTRY_ID = "entryId";

    private DatabaseDataSource dataSource;
    private PreferenceHelper preferenceHelper;
    private Entry entry;

    private TextView textViewDate;
    private TextView textViewTime;
    private TextView textViewNote;
    private LinearLayout layoutMeasurements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Get entry and all of its measurements
            if(extras.getLong(EXTRA_ENTRY_ID) != 0L) {
                dataSource = new DatabaseDataSource(this);
                preferenceHelper = new PreferenceHelper(this);

                dataSource.open();
                entry = (Entry)dataSource.get(DatabaseHelper.ENTRY, extras.getLong(EXTRA_ENTRY_ID));

                if(entry == null) {
                    dataSource.close();
                    finish();
                }

                getComponents();
                initialize();
                dataSource.close();
            }
        }
        else
            finish();
    }

    private void getComponents() {
        textViewDate = (TextView)findViewById(R.id.textview_date);
        textViewTime = (TextView)findViewById(R.id.textview_time);
        textViewNote = (TextView)findViewById(R.id.textview_note);
        layoutMeasurements = (LinearLayout)findViewById(R.id.layout_measurements);
    }

    private void initialize() {
        textViewDate.setText(Helper.getDateFormat().print(entry.getDate()));
        textViewTime.setText(Helper.getTimeFormat().print(entry.getDate()));
        if(entry.getNote() != null && entry.getNote().length() > 0)
            textViewNote.setText(entry.getNote());

        LayoutInflater inflater = getLayoutInflater();
        // Pre-load image resources
        HashMap<String, Integer> imageResources = new HashMap<String, Integer>();
        for(Measurement.Category category : Measurement.Category.values()) {
            String name = category.name().toLowerCase();
            int resourceId = getResources().getIdentifier(name,
                    "drawable", getPackageName());
            imageResources.put(name, resourceId);
        }

        List<Model> models = dataSource.get(DatabaseHelper.MEASUREMENT, null,
                DatabaseHelper.ENTRY_ID + "=?", new String[]{Long.toString(entry.getId())},
                null, null, null, null);

        for(Model model : models) {
            Measurement measurement = (Measurement)model;

            View view = inflater.inflate(R.layout.fragment_measurement, layoutMeasurements, false);
            view.setTag(measurement.getCategory());

            ImageView imageViewCategory = (ImageView) view.findViewById(R.id.image);
            imageViewCategory.setImageResource(imageResources.get(measurement.getCategory().name().toLowerCase()));

            TextView textViewCategory = (TextView) view.findViewById(R.id.category);
            textViewCategory.setText(preferenceHelper.getCategoryName(measurement.getCategory()));

            TextView textViewValue = (TextView) view.findViewById(R.id.value);
            float value = preferenceHelper.formatDefaultToCustomUnit(
                    measurement.getCategory(), measurement.getValue());
            textViewValue.setText(preferenceHelper.getDecimalFormat(measurement.getCategory()).format(value));

            // Highlight extrema
            if(measurement.getCategory() == Measurement.Category.BloodSugar && preferenceHelper.limitsAreHighlighted()) {
                if(measurement.getValue() > preferenceHelper.getLimitHyperglycemia())
                    textViewValue.setTextColor(getResources().getColor(R.color.red));
                else if(measurement.getValue() < preferenceHelper.getLimitHypoglycemia())
                    textViewValue.setTextColor(getResources().getColor(R.color.blue));
            }

            layoutMeasurements.addView(view, layoutMeasurements.getChildCount());
        }
    }
}
