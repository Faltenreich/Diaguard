package com.faltenreich.diaguard.database.measurements;

/**
 * Created by Filip on 30.06.2015.
 */
public interface ICustomizable {
    /**
     * Convert a value from the default / database value according to the currently selected unit
     * @return Value converted according to user preferences
     */
    float getValueForUser();
}
