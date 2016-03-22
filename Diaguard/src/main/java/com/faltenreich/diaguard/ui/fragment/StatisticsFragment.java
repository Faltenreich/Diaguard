package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.activity.StatisticsActivity;

/**
 * Created by Faltenreich on 16.03.2016.
 */
public class StatisticsFragment extends BaseFragment {

    public static final String BUNDLE_ARGUMENT_CATEGORY = "BUNDLE_ARGUMENT_CATEGORY";
    public static final String BUNDLE_ARGUMENT_TIME_INTERVAL = "BUNDLE_ARGUMENT_TIME_INTERVAL";

    public static StatisticsFragment newInstance(Measurement.Category category, StatisticsActivity.TimeInterval timeInterval) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(StatisticsFragment.BUNDLE_ARGUMENT_CATEGORY, category);
        bundle.putSerializable(StatisticsFragment.BUNDLE_ARGUMENT_TIME_INTERVAL, timeInterval);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Measurement.Category category;
    private StatisticsActivity.TimeInterval timeInterval;

    public StatisticsFragment() {
        super(R.layout.fragment_statistics, R.string.statistics);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContent();
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.category = (Measurement.Category) bundle.getSerializable(BUNDLE_ARGUMENT_CATEGORY);
            this.timeInterval = (StatisticsActivity.TimeInterval) bundle.getSerializable(BUNDLE_ARGUMENT_TIME_INTERVAL);
        }
    }

    public void setTimeInterval(StatisticsActivity.TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    public void updateContent() {

    }
}
