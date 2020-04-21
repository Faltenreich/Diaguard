package com.faltenreich.diaguard.shared.view.search;

public interface Searchable {
    String getQuery();
    void setQuery(String query, boolean submit);
    void focusSearchField();
}
