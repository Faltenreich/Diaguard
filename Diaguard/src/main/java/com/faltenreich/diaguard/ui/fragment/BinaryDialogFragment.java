package com.faltenreich.diaguard.ui.fragment;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.faltenreich.diaguard.ui.view.DialogButton;

abstract class BinaryDialogFragment extends BaseDialogFragment {

    BinaryDialogFragment(@StringRes int titleResId, @LayoutRes int layoutResId) {
        super(titleResId, layoutResId);
    }

    @Nullable
    @Override
    protected DialogButton createPositiveButton() {
        return new DialogButton(android.R.string.ok, new DialogButton.DialogButtonListener() {
            @Override
            public void onClick() {
                onPositiveButtonClick();
            }
        });
    }

    protected abstract void onPositiveButtonClick();
}
