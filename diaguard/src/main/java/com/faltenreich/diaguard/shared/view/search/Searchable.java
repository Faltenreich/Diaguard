package com.faltenreich.diaguard.shared.view.search;

public interface Searchable {
    String getQuery();
    void setQuery(String query, boolean submit);
    void setHint(String hint);
    void setShadow(boolean isEnabled);
    void setSearchListener(SearchListener listener);
    void focusSearchField();
}
