package com.faltenreich.diaguard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.NewEventActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.database.Model;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;

import java.util.HashMap;
import java.util.List;

public class EntryDetailFragment extends Fragment {

    public static final String ENTRY_ID = "entryId";

    private DatabaseDataSource dataSource;
    private PreferenceHelper preferenceHelper;
    private Entry entry;

    private TextView textViewTime;
    private TextView textViewNote;
    private LinearLayout layoutMeasurements;
    private ImageButton buttonEdit;

    public static EntryDetailFragment newInstance(long entryId) {
        EntryDetailFragment fragment = new EntryDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ENTRY_ID, entryId);
        fragment.setArguments(args);
        return fragment;
    }

    public EntryDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_detail, container, false);
        setHasOptionsMenu(true);
        if (getArguments() != null && getArguments().getLong(ENTRY_ID) > 0) {
            long entryId = getArguments().getLong(ENTRY_ID);
            dataSource = new DatabaseDataSource(getActivity());
            preferenceHelper = new PreferenceHelper(getActivity());
            dataSource.open();
            entry = (Entry)dataSource.get(DatabaseHelper.ENTRY, entryId);
            dataSource.close();

            getComponents(view);
            initializeGUI();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    private void getComponents(View parentView) {
        textViewTime = (TextView)parentView.findViewById(R.id.textview_time);
        textViewNote = (TextView)parentView.findViewById(R.id.textview_note);
        layoutMeasurements = (LinearLayout)parentView.findViewById(R.id.layout_measurements);
        buttonEdit = (ImageButton)parentView.findViewById(R.id.button_edit);
    }

    private void initializeGUI() {
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewEventActivity.class);
                intent.putExtra(NewEventActivity.EXTRA_ENTRY, entry.getId());
                startActivity(intent);
            }
        });
    }

    private void initialize() {
        String time = Helper.getDateFormat().print(entry.getDate()) + " " +
                Helper.getTimeFormat().print(entry.getDate());
        textViewTime.setText(time);
        if(entry.getNote() != null && entry.getNote().length() > 0)
            textViewNote.setText(entry.getNote());

        LayoutInflater inflater = getLayoutInflater(getArguments());
        // Pre-load image resources
        HashMap<String, Integer> imageResources = new HashMap<String, Integer>();
        for(Measurement.Category category : Measurement.Category.values()) {
            String name = category.name().toLowerCase();
            int resourceId = getResources().getIdentifier(name,
                    "drawable", getActivity().getPackageName());
            imageResources.put(name, resourceId);
        }

        dataSource.open();
        List<Model> models = dataSource.get(DatabaseHelper.MEASUREMENT, null,
                DatabaseHelper.ENTRY_ID + "=?", new String[]{Long.toString(entry.getId())},
                null, null, null, null);
        dataSource.close();

        layoutMeasurements.removeAllViews();
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

            TextView textViewUnit = (TextView) view.findViewById(R.id.unit);
            textViewUnit.setText(preferenceHelper.getUnitAcronym(measurement.getCategory()));

            layoutMeasurements.addView(view, layoutMeasurements.getChildCount());
        }
    }
}
