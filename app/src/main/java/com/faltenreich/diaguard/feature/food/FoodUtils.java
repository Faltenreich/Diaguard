package com.faltenreich.diaguard.feature.food;

public class FoodUtils {

    private static final float FACTOR_KCAL_TO_KJ = 4.184f;

    public static float parseKcalToKj(float kcal) {
        return kcal * FACTOR_KCAL_TO_KJ;
    }

    public static float parseKjToKcal(float kJ) {
        return kJ / FACTOR_KCAL_TO_KJ;
    }
}
