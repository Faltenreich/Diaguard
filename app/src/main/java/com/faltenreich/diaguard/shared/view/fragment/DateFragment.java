package com.faltenreich.diaguard.shared.view.fragment;

import android.content.res.Configuration;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.faltenreich.diaguard.feature.datetime.DatePickerFragment;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;
import com.faltenreich.diaguard.feature.navigation.MainButton;
import com.faltenreich.diaguard.feature.navigation.MainButtonProperties;

import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;

public abstract class DateFragment extends BaseFragment implements BaseFragment.ToolbarCallback, MainButton {

    private DateTime day;

    protected DateFragment(@LayoutRes int layoutResourceId, @StringRes int titleResourceId, @MenuRes int menuRes) {
        super(layoutResourceId, titleResourceId, menuRes);
        try {
            this.day = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);
        } catch (IllegalFieldValueException exception) {
            // Fixes IllegalFieldValueException on Motorola Moto G6 Play
            this.day = DateTime.now();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateLabels();
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

    protected void updateLabels() {
        setTitle(getTitle());
    }

    private void showDatePicker() {
        DatePickerFragment.newInstance(day, dateTime -> {
            if (dateTime != null) {
                goToDay(dateTime);
            }
        }).show(getParentFragmentManager());
    }

    @Override
    public void action() {
        showDatePicker();
    }

    @Override
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.addButton(view -> {
            if (getContext() != null) {
                // Date will not be passed through to compensate negative user feedback
                EntryEditActivity.show(getContext());
            }
        });
    }
}
