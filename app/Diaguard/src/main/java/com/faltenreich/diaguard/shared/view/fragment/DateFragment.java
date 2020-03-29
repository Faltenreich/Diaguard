package com.faltenreich.diaguard.shared.view.fragment;

import android.content.res.Configuration;
import android.view.MenuItem;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.DatePickerFragment;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;
import com.faltenreich.diaguard.feature.navigation.MainButton;
import com.faltenreich.diaguard.feature.navigation.MainButtonProperties;

import org.joda.time.DateTime;

public abstract class DateFragment extends BaseFragment implements BaseFragment.ToolbarCallback, MainButton {

    private DateTime day;

    protected DateFragment(@LayoutRes int layoutResourceId, @StringRes int titleResourceId) {
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

    protected void updateLabels() {
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
                EntryEditActivity.show(getContext());
            }
        });
    }
}
