package com.faltenreich.diaguard.ui.view.chart;

import android.content.Context;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by Filip on 23.05.2015.
 */
public class ChartMarkerView extends MarkerView {

    private TextView textViewValue;

    public ChartMarkerView (Context context) {
        super(context, R.layout.view_chart_marker);
        textViewValue = findViewById(R.id.textview_value);
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        textViewValue.setText((int) entry.getY() + " " + PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.BLOODSUGAR));
    }

    @Override
    public MPPointF getOffset() {
        int xOffset = - (getWidth() / 2);
        int yOffset = - getHeight() - (getHeight() / 4);
        return new MPPointF(xOffset, yOffset);
    }
}
