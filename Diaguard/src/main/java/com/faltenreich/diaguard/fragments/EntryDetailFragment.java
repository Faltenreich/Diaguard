package com.faltenreich.diaguard.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.MainActivity;
import com.faltenreich.diaguard.NewEventActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.database.Model;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.ViewHelper;

import java.util.HashMap;
import java.util.List;

public class EntryDetailFragment extends Fragment {

    public static final String ENTRY_ID = "entryId";

    private DatabaseDataSource dataSource;
    private PreferenceHelper preferenceHelper;
    private Entry entry;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(!ViewHelper.isLargeScreen(getActivity()))
            inflater.inflate(R.menu.edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    private void getComponents(View parentView) {
        textViewNote = (TextView)parentView.findViewById(R.id.textview_note);
        layoutMeasurements = (LinearLayout)parentView.findViewById(R.id.layout_measurements);
        buttonEdit = (ImageButton)parentView.findViewById(R.id.button_edit);
    }

    private void initializeGUI() {
        if(ViewHelper.isLargeScreen(getActivity())) {
            buttonEdit.setVisibility(View.VISIBLE);
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editEntry();
                }
            });
        }
        else {
            buttonEdit.setVisibility(View.GONE);
        }
    }

    private void editEntry() {
        Intent intent = new Intent(getActivity(), NewEventActivity.class);
        intent.putExtra(NewEventActivity.EXTRA_ENTRY, entry.getId());
        startActivity(intent);
    }

    private void deleteEvent() {
        if (entry != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.entry_delete);
            builder.setMessage(R.string.entry_delete_desc);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dataSource.open();
                    dataSource.delete(entry);
                    dataSource.close();
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.ENTRY_DELETED, true);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void initialize() {
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

        if(entry.getNote() != null && entry.getNote().length() > 0) {
            textViewNote.setText(entry.getNote());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteEvent();
                return true;
            case R.id.action_edit:
                editEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
