package com.faltenreich.diaguard.ui.activity;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.TextUtils;

import com.faltenreich.diaguard.R;
import com.lapism.searchview.SearchView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Filip on 15.11.13.
 */

public class FoodSearchActivity extends BaseActivity {

    @BindView(R.id.search_view) SearchView searchView;

    public FoodSearchActivity() {
        super(R.layout.activity_food_search);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                String voiceInput = results.get(0);
                if (!TextUtils.isEmpty(voiceInput)) {
                    searchView.setQuery(voiceInput, true);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}