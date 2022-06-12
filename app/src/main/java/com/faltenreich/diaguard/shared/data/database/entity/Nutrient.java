package com.faltenreich.diaguard.shared.data.database.entity;

import android.content.Context;
import android.util.Log;

import androidx.annotation.StringRes;

import com.faltenreich.diaguard.R;

public enum Nutrient {
    CARBOHYDRATES(R.string.carbohydrates),
    SUGAR(R.string.sugar),
    ENERGY(R.string.energy),
    FAT(R.string.fat),
    FAT_SATURATED(R.string.fat_saturated),
    FIBER(R.string.fiber),
    PROTEINS(R.string.proteins),
    SALT(R.string.salt),
    SODIUM(R.string.sodium);

    private final int textResId;

    Nutrient(@StringRes int textResId) {
        this.textResId = textResId;
    }

    public String getLabel(Context context) {
        return context.getString(textResId);
    }

    public @StringRes
    int getUnitResId() {
        switch (this) {
            case ENERGY:
                return R.string.energy_acronym;
            case SODIUM:
                return R.string.milligrams_acronym;
            default:
                return R.string.grams_acronym;
        }
    }

    public Float getValue(Food food) {
        switch (this) {
            case CARBOHYDRATES:
                return food.getCarbohydrates();
            case ENERGY:
                return food.getEnergy();
            case FAT:
                return food.getFat();
            case FAT_SATURATED:
                return food.getFatSaturated();
            case FIBER:
                return food.getFiber();
            case PROTEINS:
                return food.getProteins();
            case SALT:
                return food.getSalt();
            case SODIUM:
                return food.getSodium() != null ? food.getSodium() * 1000 : null;
            case SUGAR:
                return food.getSugar();
            default:
                return null;
        }
    }

    public void setValue(Food food, float value) {
        switch (this) {
            case CARBOHYDRATES:
                food.setCarbohydrates(value);
                break;
            case ENERGY:
                food.setEnergy(value);
                break;
            case FAT:
                food.setFat(value);
                break;
            case FAT_SATURATED:
                food.setFatSaturated(value);
                break;
            case FIBER:
                food.setFiber(value);
                break;
            case PROTEINS:
                food.setProteins(value);
                break;
            case SALT:
                food.setSalt(value);
                break;
            case SODIUM:
                food.setSodium(value > 0 ? value / 1000 : value);
                break;
            case SUGAR:
                food.setSugar(value);
                break;
            default:
                Log.e(Nutrient.class.getSimpleName(), "Unsupported nutrient for applyValue(): " + this);
        }
    }
}
