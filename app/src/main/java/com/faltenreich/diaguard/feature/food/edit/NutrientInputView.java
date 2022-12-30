package com.faltenreich.diaguard.feature.food.edit;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInputView;

@SuppressLint("ViewConstructor")
public class NutrientInputView extends StickyHintInputView {

    private final Food.Nutrient nutrient;
    private final Float initialValue;

    public NutrientInputView(Context context, @NonNull Food.Nutrient nutrient, @Nullable Float value) {
        super(context);
        this.nutrient = nutrient;
        this.initialValue = value;
        init();
    }

    @NonNull
    public Food.Nutrient getNutrient() {
        return nutrient;
    }

    @Nullable
    public Float getValue() {
        return getEditText().getNumber();
    }

    public void setValue(float value) {
        getEditText().setText(FloatUtils.parseFloat(value));
    }

    private void init() {
        setHint(nutrient.getLabel(getContext()));
        setSuffixText(String.format("%s %s",
            getContext().getString(nutrient.getUnitResId()),
            getContext().getString(R.string.per_100g)
        ));

        if (initialValue != null && initialValue >= 0) {
            setValue(initialValue);
        }
    }
}
