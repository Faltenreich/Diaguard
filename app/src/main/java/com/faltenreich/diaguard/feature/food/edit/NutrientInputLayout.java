package com.faltenreich.diaguard.feature.food.edit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.entity.Nutrient;

import java.util.HashMap;
import java.util.Map;

public class NutrientInputLayout extends LinearLayout {

    public NutrientInputLayout(Context context) {
        super(context);
    }

    public NutrientInputLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NutrientInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void clearNutrients() {
        removeAllViews();
    }

    public void addNutrient(@NonNull Nutrient nutrient, @Nullable Float value) {
        addView(new NutrientInputView(getContext(), nutrient, value));
    }

    public Map<Nutrient, Float> getValues() {
        Map<Nutrient, Float> values = new HashMap<>();
        for (int index = 0; index < getChildCount(); index++) {
            View view = getChildAt(index);
            if (view instanceof NutrientInputView) {
                NutrientInputView nutrientInputView = (NutrientInputView) view;
                values.put(nutrientInputView.getNutrient(), nutrientInputView.getValue());
            }
        }
        return values;
    }
}