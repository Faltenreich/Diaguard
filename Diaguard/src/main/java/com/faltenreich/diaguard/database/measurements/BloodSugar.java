package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Filip on 09.08.2014.
 */
@DatabaseTable
public class BloodSugar extends Measurement implements ICustomizable {

    public static final String MGDL = "mgDl";

    @DatabaseField
    private float mgDl;

    public float getMgDl() {
        return mgDl;
    }

    public void setMgDl(float mgDl) {
        this.mgDl = mgDl;
    }

    public Category getMeasurementType() {
        return Category.BloodSugar;
    }

    @Override
    public float getValueForUser() {
        return PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.BloodSugar, mgDl);
    }
}
