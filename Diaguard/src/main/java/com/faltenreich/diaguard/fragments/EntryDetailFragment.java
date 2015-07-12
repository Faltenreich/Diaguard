package com.faltenreich.diaguard.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.MainActivity;
import com.faltenreich.diaguard.NewEventActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseFacade;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.measurements.BloodSugar;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.ViewHelper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class EntryDetailFragment extends BaseFragment {

    public static final String EXTRA_ENTRY = "com.faltenreich.diaguard.EntryDetailActivity.ENTRY";

    public Entry entry;

    private TextView textViewNote;
    private LinearLayout layoutMeasurements;

    private HashMap<String, Integer> imageResources;

    // Large only
    private ViewGroup layoutLarge;
    private TextView textViewDate;
    private ImageView buttonEdit;
    private ImageView buttonDelete;

    public static EntryDetailFragment newInstance(long entryId) {
        EntryDetailFragment fragment = new EntryDetailFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_ENTRY, entryId);
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
        if (getArguments() != null && getArguments().getLong(EXTRA_ENTRY) > 0) {
            long entryId = getArguments().getLong(EXTRA_ENTRY);
            try {
                entry = DatabaseFacade.getInstance().getDao(Entry.class).queryForId(entryId);
            } catch (SQLException exception) {
                Log.e("EntryDetailFragment", exception.getMessage());
            }

            getComponents(view);
            initializeGUI();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(!ViewHelper.isLargeScreen(getActivity())) {
            menu.clear();
            inflater.inflate(R.menu.edit, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(entry != null) {
            initialize();
        }
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.entry);
    }

    @Override
    public boolean hasAction() {
        return false;
    }

    @Override
    public void action(View view) {}

    private void getComponents(View parentView) {
        textViewNote = (TextView) parentView.findViewById(R.id.textview_note);
        layoutMeasurements = (LinearLayout) parentView.findViewById(R.id.layout_measurements);
        layoutLarge = (ViewGroup) parentView.findViewById(R.id.layout_large);
        textViewDate = (TextView) parentView.findViewById(R.id.textview_date);
        buttonEdit = (ImageView) parentView.findViewById(R.id.button_edit);
        buttonDelete = (ImageView) parentView.findViewById(R.id.button_delete);
    }

    private void initializeGUI() {
        if(ViewHelper.isLargeScreen(getActivity())) {
            layoutLarge.setVisibility(View.VISIBLE);
            textViewDate.setText(Helper.getDateFormat().print(entry.getDate()) + " " +
                    Helper.getTimeFormat().print(entry.getDate()));
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editEntry();
                }
            });
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteEntry();
                }
            });
        }
        else {
            layoutLarge.setVisibility(View.GONE);
        }
    }

    private void initialize() {
        // Pre-load image resources
        imageResources = new HashMap<>();
        for(Measurement.Category category : Measurement.Category.values()) {
            imageResources.put(category.name(), PreferenceHelper.getInstance().getCategoryImageResourceId(category));
        }

        layoutMeasurements.removeAllViews();
        try {
            List<Measurement> measurements = DatabaseFacade.getInstance().getMeasurements(entry);
            for(Measurement measurement : measurements) {
                addMeasurement(measurement);
            }
        } catch (SQLException exception) {
            Log.e("EntryDetailFragment", exception.getMessage());
        }

        if(entry.getNote() != null && entry.getNote().length() > 0) {
            textViewNote.setText(entry.getNote());
        }
    }

    private void addMeasurement(Measurement measurement) {
        Measurement.Category category = measurement.getMeasurementType();
        View view = getLayoutInflater(getArguments()).inflate(R.layout.fragment_measurement, layoutMeasurements, false);
        view.setTag(category);

        ImageView imageViewCategory = (ImageView) view.findViewById(R.id.image);
        imageViewCategory.setImageResource(imageResources.get(category.name()));
        int backgroundColor = getResources().getColor(R.color.gray_dark);
        if (category == Measurement.Category.BloodSugar) {
            BloodSugar bloodSugar = (BloodSugar) measurement;
            if (bloodSugar.getMgDl() > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                backgroundColor = getResources().getColor(R.color.red);
            } else if (bloodSugar.getMgDl() < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                backgroundColor = getResources().getColor(R.color.blue);
            } else {
                backgroundColor = getResources().getColor(R.color.green);
            }
        }
        imageViewCategory.setColorFilter(backgroundColor, PorterDuff.Mode.SRC_ATOP.SRC_ATOP);

        TextView textViewCategory = (TextView) view.findViewById(R.id.category);
        textViewCategory.setText(PreferenceHelper.getInstance().getCategoryName(category));

        TextView textViewValue = (TextView) view.findViewById(R.id.value);
        textViewValue.setText(measurement.toString());

        // Highlight extrema
        if(category == Measurement.Category.BloodSugar && PreferenceHelper.getInstance().limitsAreHighlighted()) {
            BloodSugar bloodSugar = (BloodSugar) measurement;
            if(bloodSugar.getMgDl() > PreferenceHelper.getInstance().getLimitHyperglycemia())
                textViewValue.setTextColor(getResources().getColor(R.color.red));
            else if(bloodSugar.getMgDl() < PreferenceHelper.getInstance().getLimitHypoglycemia())
                textViewValue.setTextColor(getResources().getColor(R.color.blue));
        }

        TextView textViewUnit = (TextView) view.findViewById(R.id.unit);
        textViewUnit.setText(PreferenceHelper.getInstance().getUnitAcronym(category));

        layoutMeasurements.addView(view, layoutMeasurements.getChildCount());
    }

    public void editEntry() {
        if (entry != null) {
            Intent intent = new Intent(getActivity(), NewEventActivity.class);
            intent.putExtra(NewEventActivity.EXTRA_ENTRY, entry.getId());
            startActivity(intent);
        }
    }

    public void deleteEntry() {
        if (entry != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.entry_delete);
            builder.setMessage(R.string.entry_delete_desc);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //dataSource.delete(entry);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteEntry();
                return true;
            case R.id.action_edit:
                editEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
