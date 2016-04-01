package com.faltenreich.diaguard.util.thread;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.ui.view.chart.PercentValueFormatter;
import com.faltenreich.diaguard.util.TimeSpan;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 16.03.2016.
 */

public class UpdateBloodSugarPieChartTask extends BaseAsyncTask<Void, Void, PieData> {

    private TimeSpan timeSpan;

    public UpdateBloodSugarPieChartTask(Context context, OnAsyncProgressListener<PieData> onAsyncProgressListener, TimeSpan timeSpan) {
        super(context, onAsyncProgressListener);
        this.timeSpan = timeSpan;
    }

    @Override
    protected PieData doInBackground(Void... params) {
        List<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        float limitHypo = PreferenceHelper.getInstance().getLimitHypoglycemia();
        float limitHyper = PreferenceHelper.getInstance().getLimitHyperglycemia();

        Interval interval = timeSpan.getPastInterval(DateTime.now());
        long targetCount = EntryDao.getInstance().countBetween(interval.getStart(), interval.getEnd(), limitHypo,  limitHyper);
        long hypoCount = EntryDao.getInstance().countBelow(interval.getStart(), interval.getEnd(), limitHypo);
        long hyperCount = EntryDao.getInstance().countAbove(interval.getStart(), interval.getEnd(), limitHyper);

        List<Integer> colors = new ArrayList<>();

        if (targetCount > 0) {
            entries.add(new Entry(targetCount, entries.size()));
            colors.add(ContextCompat.getColor(getContext(), R.color.green));
            labels.add(getContext().getString(R.string.target_area));
        }

        if (hypoCount > 0) {
            entries.add(new Entry(hypoCount, entries.size()));
            colors.add(ContextCompat.getColor(getContext(), R.color.blue));
            labels.add(getContext().getString(R.string.hypo));
        }

        if (hyperCount > 0) {
            entries.add(new Entry(hyperCount, entries.size()));
            colors.add(ContextCompat.getColor(getContext(), R.color.red));
            labels.add(getContext().getString(R.string.hyper));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        dataSet.setValueTextSize(13);
        dataSet.setValueFormatter(new PercentValueFormatter());

        return new PieData(labels, dataSet);
    }
}