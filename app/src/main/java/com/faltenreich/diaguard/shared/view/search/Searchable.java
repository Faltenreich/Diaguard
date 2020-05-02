package com.faltenreich.diaguard.shared.view.search;

import java.util.List;

public interface Searchable {
    void setSearchListener(SearchViewListener listener);
    void setAction(SearchViewAction action);
    String getQuery();
    void setQuery(String query, boolean submit);
    void setHint(String hint);
    void setSuggestions(List<String> suggestions);
    void focusSearchField();
}
