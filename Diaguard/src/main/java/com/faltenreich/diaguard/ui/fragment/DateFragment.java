package com.faltenreich.diaguard.ui.fragment;

import android.content.res.Configuration;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import android.view.MenuItem;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.view.MainButton;
import com.faltenreich.diaguard.ui.view.MainButtonProperties;

import org.joda.time.DateTime;

public abstract class DateFragment extends BaseFragment implements BaseFragment.ToolbarCallback, MainButton {

    private DateTime day;

    DateFragment(@LayoutRes int layoutResourceId, @StringRes int titleResourceId) {
        super(layoutResourceId, titleResourceId, R.menu.date);
        this.day = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateLabels();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_today:
                goToDay(DateTime.now());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected DateTime getDay() {
        return day;
    }

    protected void setDay(DateTime day) {
        this.day = day;
    }

    @CallSuper
    protected void goToDay(DateTime day) {
        setDay(day);
        updateLabels();
    }

    void updateLabels() {
        setTitle(getTitle());
    }

    private void showDatePicker() {
        if (getActivity() != null) {
            DatePickerFragment.newInstance(day, dateTime -> {
                if (dateTime != null) {
                    goToDay(dateTime);
                }
            }).show(getActivity().getSupportFragmentManager());
        }
    }

    @Override
    public void action() {
        showDatePicker();
    }

    @Override
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.addButton(view -> {
            if (getContext() != null) {
                EntryActivity.show(getContext());
            }
        });
    }
}
