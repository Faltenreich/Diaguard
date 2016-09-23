package com.faltenreich.diaguard.ui.activity;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.TextUtils;

import com.faltenreich.diaguard.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Filip on 15.11.13.
 */

public class FoodSearchActivity extends BaseActivity {

    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    public FoodSearchActivity() {
        super(R.layout.activity_food_search);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String query = matches.get(0);
                if (!TextUtils.isEmpty(query)) {
                    searchView.setQuery(query, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}