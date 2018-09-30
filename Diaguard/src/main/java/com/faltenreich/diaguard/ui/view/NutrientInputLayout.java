package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.faltenreich.diaguard.data.entity.Food;

import java.util.HashMap;
import java.util.Map;

public class NutrientInputLayout extends LinearLayout {

    public NutrientInputLayout(Context context) {
        super(context);
        init();
    }

    public NutrientInputLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NutrientInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public void addNutrient(@NonNull Food.Nutrient nutrient, @Nullable Float value) {
        addView(new NutrientInputView(getContext(), nutrient, value));
    }

    public Map<Food.Nutrient, Float> getValues() {
        Map<Food.Nutrient, Float> values = new HashMap<>();
        for (int index = 0; index < getChildCount(); index++) {
            View view = getChildAt(index);
            if (view instanceof NutrientInputView) {
                NutrientInputView nutrientInputView = (NutrientInputView) view;
                values.put(nutrientInputView.getNutrient(), nutrientInputView.getValue());
            }
        }
        return values;
    }

    public NutrientInputView getInputView(Food.Nutrient nutrient) {
        for (int index = 0; index < getChildCount(); index++) {
            View view = getChildAt(index);
            if (view instanceof NutrientInputView) {
                NutrientInputView nutrientInputView = (NutrientInputView) view;
                if (nutrientInputView.getNutrient() == nutrient) {
                    return nutrientInputView;
                }
            }
        }
        return null;
    }
}