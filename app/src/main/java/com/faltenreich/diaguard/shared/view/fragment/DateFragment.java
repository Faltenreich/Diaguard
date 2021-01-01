package com.faltenreich.diaguard.shared.view.fragment;

import android.content.Intent;
import android.content.res.Configuration;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.faltenreich.diaguard.feature.datetime.DatePickerFragment;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditIntentFactory;
import com.faltenreich.diaguard.feature.navigation.MainButton;
import com.faltenreich.diaguard.feature.navigation.MainButtonProperties;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;

import org.joda.time.DateTime;

public abstract class DateFragment<BINDING extends ViewBinding>
    extends BaseFragment<BINDING>
    implements ToolbarDescribing, BaseFragment.ToolbarCallback, MainButton {

    private DateTime day;

    protected DateFragment(@LayoutRes int layoutResourceId) {
        super(layoutResourceId);
        this.day = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);
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
        setTitle(getToolbarProperties().getTitle());
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
                Intent intent = EntryEditIntentFactory.newInstance(getContext());
                startActivity(intent);
            }
        });
    }
}
