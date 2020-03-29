package com.faltenreich.diaguard.shared.data.database.dao;

/**
 * Created by Faltenreich on 03.04.2016.
 */
public enum SqlFunction {
    AVG("avg"),
    SUM("sum");

    public String function;

    SqlFunction(String function) {
        this.function = function;
    }
}
