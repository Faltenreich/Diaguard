package com.faltenreich.diaguard.ui.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.view.DayOfMonthDrawable;
import com.faltenreich.diaguard.ui.view.MainButton;
import com.faltenreich.diaguard.ui.view.MainButtonProperties;

import org.joda.time.DateTime;

public abstract class DateFragment extends BaseFragment implements BaseFragment.ToolbarCallback, MainButton {

    private DateTime day;

    public DateFragment(@LayoutRes int layoutResourceId, @StringRes int titleResourceId) {
        super(layoutResourceId, titleResourceId, R.menu.date);
        this.day = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);
    }

    public void setDay(DateTime day) {
        this.day = day;
        updateLabels();
    }

    public DateTime getDay() {
        return day;
    }

    @Override
    public void onViewCreated (@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateLabels();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateLabels();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void action() {
        showDatePicker();
    }

    @Override
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.addButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTime now = DateTime.now();
                DateTime dateTime = day
                        .withHourOfDay(now.getHourOfDay())
                        .withMinuteOfHour(now.getMinuteOfHour())
                        .withSecondOfMinute(now.getSecondOfMinute());
                EntryActivity.show(getContext(), view, dateTime);
            }
        });
    }

    @CallSuper
    protected void goToDay(DateTime day) {
        this.day = day;
        updateLabels();
    }

    private void setTodayIcon(LayerDrawable icon, Context context) {
        DayOfMonthDrawable today = new DayOfMonthDrawable(context);
        today.setDayOfMonth(DateTime.now().dayOfMonth().get());
        icon.mutate();
        icon.setDrawableByLayerId(R.id.today_icon_day, today);
    }

    private void showDatePicker() {
        DatePickerFragment.newInstance(day, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                goToDay(DateTime.now().withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day));
            }
        }).show(getActivity().getSupportFragmentManager());
    }

    protected abstract void updateLabels();
}
