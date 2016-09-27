package com.faltenreich.diaguard.ui.fragment;


import android.support.v4.app.Fragment;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.view.chart.ChartViewPager;
import com.faltenreich.diaguard.util.ViewHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends DateFragment implements ChartViewPager.ChartViewPagerCallback {

    @BindView(R.id.viewpager) ChartViewPager viewPager;

    public ChartFragment() {
        super(R.layout.fragment_chart);
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.timeline);
    }

    @Override
    protected void initialize() {
        super.initialize();
        viewPager.setup(getChildFragmentManager(), this);
    }

    @Override
    protected void goToDay(DateTime day) {
        super.goToDay(day);
        viewPager.setDay(day);
    }

    @Override
    public void onDaySelected(DateTime day) {
        if (day != null) {
            setDay(day);
        }
    }

    @Override
    protected void updateLabels() {
        if (isAdded() && getActionView() != null) {
            boolean showShortText = !ViewHelper.isLandscape(getActivity()) && !ViewHelper.isLargeScreen(getActivity());
            String weekDay = showShortText ?
                    getDay().dayOfWeek().getAsShortText() :
                    getDay().dayOfWeek().getAsText();
            String date = DateTimeFormat.mediumDate().print(getDay());
            getActionView().setText(String.format("%s, %s", weekDay, date));
        }
    }
}
