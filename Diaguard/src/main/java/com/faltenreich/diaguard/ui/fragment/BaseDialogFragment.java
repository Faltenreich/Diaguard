package com.faltenreich.diaguard.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.faltenreich.diaguard.ui.view.DialogButton;

import butterknife.ButterKnife;

abstract class BaseDialogFragment extends DialogFragment {

    protected View contentView;

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
            return builder.create();
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
    protected DialogButton createPositiveButton() {
        return new DialogButton(android.R.string.ok);
    }

    @Nullable
    protected DialogButton createNegativeButton() {
        return new DialogButton(android.R.string.cancel, new DialogButton.DialogButtonListener() {
            @Override
            public void onClick() {
                dismiss();
            }
        });
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
        positiveButton = createPositiveButton();
        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton.getLabelResId(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (positiveButton.getListener() != null) {
                        positiveButton.getListener().onClick();
                    }
                }
            });
        }
        negativeButton = createNegativeButton();
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton.getLabelResId(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (negativeButton.getListener() != null) {
                        negativeButton.getListener().onClick();
                    }
                }
            });
        }
        neutralButton = createNeutralButton();
        if (neutralButton != null) {
            builder.setNeutralButton(neutralButton.getLabelResId(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (neutralButton.getListener() != null) {
                        neutralButton.getListener().onClick();
                    }
                }
            });
        }
    }

    private void invalidateButtons(AlertDialog alertDialog) {
        if (positiveButton != null) {
            positiveButton.setButton(alertDialog.getButton(Dialog.BUTTON_POSITIVE));
        }
        if (negativeButton != null) {
            negativeButton.setButton(alertDialog.getButton(Dialog.BUTTON_NEGATIVE));
        }
        if (neutralButton != null) {
            neutralButton.setButton(alertDialog.getButton(Dialog.BUTTON_NEUTRAL));
        }
    }

    private void initContentView(AlertDialog.Builder builder) {
        if (layoutResId >= 0) {
            contentView = LayoutInflater.from(getContext()).inflate(layoutResId, null);
            if (contentView != null) {
                builder.setView(contentView);
                ButterKnife.bind(this, contentView);
            }
        }
    }
}
