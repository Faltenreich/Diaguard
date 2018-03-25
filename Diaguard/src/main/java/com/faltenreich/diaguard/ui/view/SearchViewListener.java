package com.faltenreich.diaguard.ui.view;

/**
 * Created by Faltenreich on 15.08.2017
 */

public interface SearchViewListener {
    void onSearchViewExpanded();
    void onSearchViewCollapsed();
    void onSearchViewQueryChange(String newText);
}
