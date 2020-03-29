package com.faltenreich.diaguard.feature.preference.bloodsugar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.preference.BloodSugarPreferenceChangedEvent;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.validation.Validator;
import com.faltenreich.diaguard.shared.view.edittext.LocalizedNumberEditText;

/**
 * Created by Filip on 04.11.13.
 */
public class BloodSugarPreference extends EditTextPreference {

    private Context context;
    private SharedPreferences sharedPreferences;

    private LocalizedNumberEditText editTextValue;

    public BloodSugarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_bloodsugar);

        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);

        editTextValue = view.findViewById(R.id.value);
        if(editTextValue == null || editTextValue.getText() == null) {
            throw new Resources.NotFoundException();
        }

        float value = FloatUtils.parseNumber(sharedPreferences.getString(getKey(), ""));
        value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, value);
        editTextValue.setText(FloatUtils.parseFloat(value));
        editTextValue.setSelection(editTextValue.getText().length());

        TextView textViewUnit = view.findViewById(R.id.unit);
        textViewUnit.setText(PreferenceHelper.getInstance().getUnitName(Category.BLOODSUGAR));
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        final AlertDialog alertDialog = (AlertDialog)getDialog();
        if(alertDialog == null || alertDialog.getButton(AlertDialog.BUTTON_POSITIVE) == null)
            throw new Resources.NotFoundException();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Validator.validateEditTextEvent(context, editTextValue, Category.BLOODSUGAR, true)) {
                    alertDialog.dismiss();
                    onDialogClosed(true);
                }
            }
        });
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            if(editTextValue == null || editTextValue.getText() == null)
                throw new Resources.NotFoundException();

            float value = FloatUtils.parseNumber(editTextValue.getText().toString());
            SharedPreferences.Editor editor = getEditor();
            if(editor == null)
                throw new Resources.NotFoundException();

            value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(Category.BLOODSUGAR, value);
            editor.putString(getKey(), Float.toString(value));
            editor.commit();

            Events.post(new BloodSugarPreferenceChangedEvent());
        }
    }
}