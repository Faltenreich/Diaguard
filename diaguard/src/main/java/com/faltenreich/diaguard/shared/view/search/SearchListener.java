package com.faltenreich.diaguard.shared.view.search;

public interface SearchListener {
    void onQueryChanged(String query);
    void onQueryClosed();
}
