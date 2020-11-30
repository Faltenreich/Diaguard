package com.faltenreich.diaguard.shared.view.fragment;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.viewbinding.ViewBinding;

import com.faltenreich.diaguard.shared.view.dialog.DialogButton;

public abstract class BaseDialogFragment<BINDING extends ViewBinding> extends DialogFragment {

    @StringRes private final int titleResId;
    @StringRes private final int messageResId;
    @LayoutRes private final int layoutResId;

    private DialogButton positiveButton;
    private DialogButton negativeButton;
    private DialogButton neutralButton;

    private BINDING binding;

    public BaseDialogFragment(@StringRes int titleResId, @StringRes int messageResId, @LayoutRes int layoutResId) {
        this.titleResId = titleResId;
        this.messageResId = messageResId;
        this.layoutResId = layoutResId;
    }

    public BaseDialogFragment(@StringRes int titleResId, @LayoutRes int layoutResId) {
        this(titleResId, -1, layoutResId);
    }

    protected BINDING getBinding() {
        return binding;
    }

    protected abstract BINDING createBinding(View view);

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            initLayout(builder);
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
        invalidateLayout();
    }

    private void initLayout(AlertDialog.Builder builder) {
        try {
            builder.setTitle(titleResId);
        } catch (Resources.NotFoundException ignored) {}

        try {
            builder.setMessage(messageResId);
        } catch (Resources.NotFoundException ignored) {}

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

        View contentView = LayoutInflater.from(getContext()).inflate(layoutResId, null);
        builder.setView(contentView);
        binding = createBinding(contentView);
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

    private void invalidateLayout() {
        AlertDialog alertDialog = getDialog() instanceof AlertDialog ? (AlertDialog) getDialog() : null;
        if (alertDialog == null) {
            return;
        }

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
}
