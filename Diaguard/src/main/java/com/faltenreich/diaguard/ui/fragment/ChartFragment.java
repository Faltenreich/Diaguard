package com.faltenreich.diaguard.ui.fragment;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.util.ViewHelper;
import com.faltenreich.diaguard.ui.view.chart.ChartViewPager;
import com.faltenreich.diaguard.ui.view.DayOfMonthDrawable;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.lang.reflect.Field;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends BaseFragment implements ChartViewPager.ChartViewPagerCallback, BaseFragment.ToolbarCallback {

    private static final String TAG = ChartFragment.class.getSimpleName();

    @Bind(R.id.viewpager)
    protected ChartViewPager viewPager;

    private DateTime day;

    public ChartFragment() {
        super(R.layout.fragment_chart);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        goToDay(day);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        updateLabels();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.date, menu);

        MenuItem menuItem = menu.findItem(R.id.action_today);
        if (menuItem != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
                setTodayIcon(icon, getActivity());
            } else {
                menuItem.setIcon(R.drawable.ic_action_today);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_today:
                goToDay(DateTime.now());
                break;
        }
        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "No ChildFragmentManager available");
        } catch (IllegalAccessException e) {
            Log.e(TAG, "No ChildFragmentManager available");
        }
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.timeline);
    }

    @Override
    public void action() {
        showDatePicker();
    }

    private void setTodayIcon(LayerDrawable icon, Context context) {
        DayOfMonthDrawable today = new DayOfMonthDrawable(context);
        today.setDayOfMonth(DateTime.now().dayOfMonth().get());
        icon.mutate();
        icon.setDrawableByLayerId(R.id.today_icon_day, today);
    }

    private void initialize() {
        day = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);
        viewPager.setup(getChildFragmentManager(), this);
    }

    private void showDatePicker() {
        DialogFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                goToDay(DateTime.now().withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day));
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, day);
        fragment.setArguments(bundle);
        fragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
    }

    private void updateLabels() {
        if(isAdded()) {
            boolean showShortText = !ViewHelper.isLandscape(getActivity()) && !ViewHelper.isLargeScreen(getActivity());
            String weekDay = showShortText ?
                    day.dayOfWeek().getAsShortText() :
                    day.dayOfWeek().getAsText();
            String date = DateTimeFormat.mediumDate().print(day);
            getActionView().setText(String.format("%s, %s", weekDay, date));
        }
    }

    private void goToDay(DateTime day) {
        this.day = day;
        viewPager.setDay(day);
        updateLabels();
    }

    @Override
    public void onDaySelected(DateTime day) {
        this.day = day;
        updateLabels();
    }
}
