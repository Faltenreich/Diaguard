package com.faltenreich.diaguard.feature.preference.decimal;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceCache;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;

import java.util.Locale;

public class DecimalPlacesPreferenceDialogFragment extends PreferenceDialogFragmentCompat {

    public static DecimalPlacesPreferenceDialogFragment newInstance(Preference preference) {
        DecimalPlacesPreferenceDialogFragment fragment = new DecimalPlacesPreferenceDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_KEY, preference.getKey());
        fragment.setArguments(arguments);
        return fragment;
    }

    private static final float DECIMAL_EXAMPLE = 0.123f;

    private TextView decimalPlacesExampleLabel;
    private SeekBar seekBar;

    private DecimalPlacesPreferenceDialogFragment() {
        super();
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        bindViews(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        initLayout();
    }

    private void bindViews(View view) {
        decimalPlacesExampleLabel = view.findViewById(R.id.decimal_places_example_label);
        seekBar = view.findViewById(R.id.decimal_places_example_seek_bar);
    }

    private void initLayout() {
        int decimalPlaces = PreferenceCache.getInstance().getDecimalPlaces();
        setDecimalPlaces(decimalPlaces);

        seekBar.setProgress(decimalPlaces);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setDecimalPlaces(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private int getDecimalPlaces() {
        return seekBar.getProgress();
    }

    private void setDecimalPlaces(int decimalPlaces) {
        Locale locale = Helper.getLocale(requireContext());
        String input = String.format(locale, "%.3f", DECIMAL_EXAMPLE);
        String output = FloatUtils.parseFloat(DECIMAL_EXAMPLE, decimalPlaces);
        requireDialog().setTitle(String.format(locale, "%s: %d", getString(R.string.decimal_places), decimalPlaces));
        decimalPlacesExampleLabel.setText(getString(R.string.decimal_places_example, input, output));
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            PreferenceStore.getInstance().setDecimalPlaces(getDecimalPlaces());
        }
    }
}
