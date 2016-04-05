package com.faltenreich.diaguard.data.entity;

/**
 * Created by Faltenreich on 05.04.2016.
 */
public enum CategoryDeprecated {

    BLOODSUGAR(Measurement.Category.BLOODSUGAR),
    BOLUS(Measurement.Category.INSULIN),
    MEAL(Measurement.Category.MEAL),
    ACTIVITY(Measurement.Category.ACTIVITY),
    HBA1C(Measurement.Category.HBA1C),
    WEIGHT(Measurement.Category.WEIGHT),
    PULSE(Measurement.Category.PULSE);

    private Measurement.Category category;

    CategoryDeprecated(Measurement.Category category) {
        this.category = category;
    }

    public Measurement.Category toUpdate() {
        return category;
    }
}
