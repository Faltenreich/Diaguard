package com.faltenreich.diaguard.shared.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.viewbinding.ViewBinding;

import com.faltenreich.diaguard.shared.view.ViewBindable;
import com.faltenreich.diaguard.shared.view.dialog.DialogButton;
import com.faltenreich.diaguard.shared.view.dialog.DialogConfig;

public abstract class BaseDialogFragment<BINDING extends ViewBinding> extends DialogFragment implements ViewBindable<BINDING> {

    private BINDING binding;

    private DialogButton positiveButton;
    private DialogButton negativeButton;
    private DialogButton neutralButton;

    @Override
    public BINDING getBinding() {
        return binding;
    }

    protected abstract BINDING createBinding(View view);

    protected abstract DialogConfig getConfig();

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
        setListeners();
    }

    private void initLayout(AlertDialog.Builder builder) {
        DialogConfig config = getConfig();

        builder.setTitle(config.getTitle());
        builder.setMessage(config.getDescription());

        positiveButton = config.getPositiveButton();
        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton.getLabelResId(), null);
        }

        negativeButton = config.getNegativeButton();
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton.getLabelResId(), null);
        }

        neutralButton = config.getNeutralButton();
        if (neutralButton != null) {
            builder.setNeutralButton(neutralButton.getLabelResId(), null);
        }

        View contentView = LayoutInflater.from(getContext()).inflate(config.getLayoutResId(), null);
        builder.setView(contentView);
        binding = createBinding(contentView);
    }

    // Listeners are set later on in order to override default behavior (like dismiss after button click)
    private void setListeners() {
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
