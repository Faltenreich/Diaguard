package com.faltenreich.diaguard.ui.view.chart;

import android.content.Context;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Filip on 23.05.2015.
 */
public class ChartMarkerView extends MarkerView {

    @BindView(R.id.marker_label)
    protected TextView labelView;

    public ChartMarkerView (Context context) {
        super(context, R.layout.view_chart_marker);
        ButterKnife.bind(this);
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        String label = (int) entry.getY() + " " + PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.BLOODSUGAR);
        labelView.setText(label);
        super.refreshContent(entry, highlight);
    }

    @Override
    public MPPointF getOffset() {
        // TODO: Adjust xOffset to stay within parent view bounds (y is already adjusted automatically)
        int xOffset = - (getWidth() / 2);
        int yOffset = - getHeight() - (getHeight() / 4);
        return new MPPointF(xOffset, yOffset);
    }
}
