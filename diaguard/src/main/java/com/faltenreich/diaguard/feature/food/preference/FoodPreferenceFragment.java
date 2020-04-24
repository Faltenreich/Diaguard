package com.faltenreich.diaguard.feature.food.preference;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.faltenreich.diaguard.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FoodPreferenceFragment extends DialogFragment {

    public interface Listener {
        void onPreferenceChanged();
    }

    public static FoodPreferenceFragment newInstance(Listener listener) {
        FoodPreferenceFragment fragment = new FoodPreferenceFragment();
        fragment.listener = listener;
        return fragment;
    }

    private Unbinder unbinder;
    private Listener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
            .setTitle(R.string.food)
            .setView(R.layout.fragment_food_preference)
            .setNegativeButton(R.string.back, null)
            .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        unbinder = ButterKnife.bind(this, requireDialog());
        initLayout();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initLayout() {

    }
}
