package com.faltenreich.diaguard.ui;

/**
 * Created by Faltenreich on 04.12.2015.
 */
public enum RequestCode {

    ENTRY_ADDED,
    ENTRY_UPDATED;

    public int getCode() {
        return this.ordinal();
    }
}
