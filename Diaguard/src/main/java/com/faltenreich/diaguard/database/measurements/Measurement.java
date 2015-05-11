package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.Model;

/**
 * Created by Filip on 11.05.2015.
 */
public abstract class Measurement extends Model {

    private int entryId;

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }
}
