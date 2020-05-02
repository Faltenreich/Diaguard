package com.faltenreich.diaguard.shared.view.activity;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.TextUtils;

import androidx.annotation.LayoutRes;

import com.faltenreich.diaguard.R;
import com.lapism.searchview.SearchView;

import java.util.List;

import butterknife.BindView;

public abstract class BaseSearchActivity extends BaseActivity {

    @BindView(R.id.search_view) SearchView searchView;

    public BaseSearchActivity(@LayoutRes int layoutResourceId) {
        super(layoutResourceId);
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