package com.faltenreich.diaguard.shared.view.search;

import java.util.List;

public interface Searchable {
    String getQuery();
    void setQuery(String query, boolean submit);
    void setHint(String hint);
    void setSearchListener(SearchListener listener);
    void setSuggestions(List<String> suggestions);
    void focusSearchField();
}
