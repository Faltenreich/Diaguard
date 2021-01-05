package com.faltenreich.diaguard.feature.navigation;

import com.faltenreich.diaguard.shared.view.search.SearchView;

public interface SearchOwner {

    SearchView getSearchView();

    default String getSearchQuery() {
        return getSearchView().getQuery();
    }

    default void setSearchQuery(String query, boolean submit) {
        getSearchView().setQuery(query, submit);
    }
}
