package com.faltenreich.diaguard.shared.view.search;

import com.lapism.searchview.SearchView;

public class SearchListenerAdapter implements SearchView.OnQueryTextListener, SearchView.OnMenuClickListener {

    private SearchListener searchListener;

    SearchListenerAdapter(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchListener.onQueryChanged(newText);
        return false;
    }

    @Override
    public void onMenuClick() {
        searchListener.onQueryClosed();
    }
}
