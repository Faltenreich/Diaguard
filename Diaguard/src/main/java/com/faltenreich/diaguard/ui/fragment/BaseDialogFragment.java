package com.faltenreich.diaguard.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.faltenreich.diaguard.ui.view.DialogButton;

import butterknife.ButterKnife;

@SuppressWarnings({"WeakerAccess", "unused"})
abstract class BaseDialogFragment extends DialogFragment {

    @StringRes private int titleResId;
    @StringRes private int messageResId;
    private DialogButton positiveButton;
    private DialogButton negativeButton;
    private DialogButton neutralButton;
    @LayoutRes private int layoutResId;

    BaseDialogFragment(@StringRes int titleResId, @StringRes int messageResId, @LayoutRes int layoutResId) {
        this.titleResId = titleResId;
        this.messageResId = messageResId;
        this.layoutResId = layoutResId;
    }

    BaseDialogFragment(@StringRes int titleResId, @LayoutRes int layoutResId) {
        this(titleResId, -1, layoutResId);
    }

    BaseDialogFragment(@StringRes int titleResId) {
        this(titleResId, -1);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            initTexts(builder);
            initButtons(builder);
            initContentView(builder);
            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        } else {
            return super.onCreateDialog(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog alertDialog = getDialog() != null && getDialog() instanceof AlertDialog ? (AlertDialog) getDialog() : null;
        if (alertDialog != null) {
            invalidateButtons(alertDialog);
        }
    }

    @Nullable
    abstract protected DialogButton createPositiveButton();

    @Nullable
    protected DialogButton createNegativeButton() {
        return new DialogButton(android.R.string.cancel, this::dismiss);
    }

    @Nullable
    protected DialogButton createNeutralButton() {
        return null;
    }

    private void initTexts(AlertDialog.Builder builder) {
        if (titleResId >= 0) {
            builder.setTitle(titleResId);
        }
        if (messageResId >= 0) {
            builder.setMessage(messageResId);
        }
    }

    private void initButtons(AlertDialog.Builder builder) {
        // Listeners are set later on / after Dialog.show() in order to override default behavior (like dismiss after button click)
        positiveButton = createPositiveButton();
        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton.getLabelResId(), null);
        }
        negativeButton = createNegativeButton();
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton.getLabelResId(), null);
        }
        neutralButton = createNeutralButton();
        if (neutralButton != null) {
            builder.setNeutralButton(neutralButton.getLabelResId(), null);
        }
    }

    private void invalidateButtons(AlertDialog alertDialog) {
        if (positiveButton != null) {
            alertDialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                if (positiveButton.getListener() != null) {
                    positiveButton.getListener().onClick();
                }
            });
        }
        if (negativeButton != null) {
            alertDialog.getButton(Dialog.BUTTON_NEGATIVE).setOnClickListener(view -> {
                if (negativeButton.getListener() != null) {
                    negativeButton.getListener().onClick();
                }
            });
        }
        if (neutralButton != null) {
            alertDialog.getButton(Dialog.BUTTON_NEUTRAL).setOnClickListener(view -> {
                if (neutralButton.getListener() != null) {
                    neutralButton.getListener().onClick();
                }
            });
        }
    }

    private void initContentView(AlertDialog.Builder builder) {
        if (layoutResId >= 0) {
            View contentView = LayoutInflater.from(getContext()).inflate(layoutResId, null);
            if (contentView != null) {
                builder.setView(contentView);
                ButterKnife.bind(this, contentView);
            }
        }
    }
}
