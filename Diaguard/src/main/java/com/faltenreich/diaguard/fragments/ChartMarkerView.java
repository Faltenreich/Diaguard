package com.faltenreich.diaguard.fragments;

import android.content.Context;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;

/**
 * Created by Filip on 23.05.2015.
 */
public class ChartMarkerView extends MarkerView {

    private TextView textViewValue;

    public ChartMarkerView (Context context) {
        super(context, R.layout.popup_chart);
        textViewValue = (TextView) findViewById(R.id.textview_value);
    }

    @Override
    public void refreshContent(Entry e, int dataSetIndex) {
        textViewValue.setText((int) e.getVal() + " " + PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.BloodSugar));
    }

    @Override
    public int getXOffset() {
        // Center the marker-view horizontally
        return - (getWidth() / 2);
    }

    @Override
    public int getYOffset() {
        // Position the marker-view to be above the selected value
        return - getHeight() - (getHeight() / 4);
    }
}
