package com.faltenreich.diaguard.feature.navigation;

import com.faltenreich.diaguard.shared.view.search.SearchViewAction;
import com.faltenreich.diaguard.shared.view.search.SearchViewListener;

import java.util.ArrayList;

public class SearchProperties {

    private final SearchViewListener listener;
    private final String hint;
    private final SearchViewAction action;
    private final ArrayList<String> suggestions;

    private SearchProperties(
        SearchViewListener listener,
        String hint,
        SearchViewAction action,
        ArrayList<String> suggestions
    ) {
        this.listener = listener;
        this.hint = hint;
        this.action = action;
        this.suggestions = suggestions;
    }

    public SearchViewListener getListener() {
        return listener;
    }

    public String getHint() {
        return hint;
    }

    public SearchViewAction getAction() {
        return action;
    }

    public ArrayList<String> getSuggestions() {
        return suggestions;
    }

    public static class Builder {

        private final SearchViewListener listener;
        private String hint;
        private SearchViewAction action;
        private ArrayList<String> suggestions;

        public Builder(SearchViewListener listener) {
            this.listener = listener;
        }

        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder setAction(SearchViewAction action) {
            this.action = action;
            return this;
        }

        public Builder setSuggestions(ArrayList<String> suggestions) {
            this.suggestions = suggestions;
            return this;
        }

        public SearchProperties build() {
            return new SearchProperties(listener, hint, action, suggestions);
        }
    }
}
