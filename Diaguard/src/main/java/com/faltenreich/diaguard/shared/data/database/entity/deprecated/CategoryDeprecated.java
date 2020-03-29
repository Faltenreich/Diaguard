package com.faltenreich.diaguard.shared.data.database.entity.deprecated;

import com.faltenreich.diaguard.shared.data.database.entity.Category;

/**
 * Created by Faltenreich on 05.04.2016.
 */
public enum CategoryDeprecated {

    BLOODSUGAR(Category.BLOODSUGAR),
    BOLUS(Category.INSULIN),
    MEAL(Category.MEAL),
    ACTIVITY(Category.ACTIVITY),
    HBA1C(Category.HBA1C),
    WEIGHT(Category.WEIGHT),
    PULSE(Category.PULSE);

    private Category category;

    CategoryDeprecated(Category category) {
        this.category = category;
    }

    public Category toUpdate() {
        return category;
    }
}
