package com.faltenreich.diaguard.feature.statistic;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.TimeSpan;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.async.BaseAsyncTask;
import com.faltenreich.diaguard.shared.data.repository.EntryRepository;
import com.faltenreich.diaguard.shared.view.chart.PercentValueFormatter;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 16.03.2016.
 */

public class BloodSugarDistributionTask extends BaseAsyncTask<Void, Void, PieData> {

    private final TimeSpan timeSpan;

    public BloodSugarDistributionTask(Context context, TimeSpan timeSpan, OnAsyncProgressListener<PieData> onAsyncProgressListener) {
        super(context, onAsyncProgressListener);
        this.timeSpan = timeSpan;
    }

    @Override
    protected PieData doInBackground(Void... params) {
        List<PieEntry> entries = new ArrayList<>();

        float limitHypo = PreferenceStore.getInstance().getLimitHypoglycemia();
        float limitHyper = PreferenceStore.getInstance().getLimitHyperglycemia();

        Interval interval = timeSpan.getInterval(DateTime.now(), -1);
        long targetCount = EntryRepository.getInstance().countBetween(interval, limitHypo,  limitHyper);
        long hypoCount = EntryRepository.getInstance().countBelow(interval, limitHypo);
        long hyperCount = EntryRepository.getInstance().countAbove(interval, limitHyper);

        List<Integer> colors = new ArrayList<>();

        if (targetCount > 0) {
            entries.add(new PieEntry(targetCount, getContext().getString(R.string.target_area), entries.size()));
            colors.add(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (hypoCount > 0) {
            entries.add(new PieEntry(hypoCount, getContext().getString(R.string.hypo), entries.size()));
            colors.add(ContextCompat.getColor(getContext(), R.color.blue));
        }

        if (hyperCount > 0) {
            entries.add(new PieEntry(hyperCount, getContext().getString(R.string.hyper), entries.size()));
            colors.add(ContextCompat.getColor(getContext(), R.color.red));
        }

        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(colors);
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        dataSet.setValueTextSize(13);
        dataSet.setValueFormatter(new PercentValueFormatter());

        return new PieData(dataSet);
    }
}