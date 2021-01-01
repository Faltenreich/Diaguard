package com.faltenreich.diaguard.feature.entry.edit;

import android.view.LayoutInflater;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityEntryEditBinding;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class EntryEditActivity extends BaseActivity<ActivityEntryEditBinding> {

    public EntryEditActivity() {
        super(R.layout.activity_entry_edit);
    }

    @Override
    protected ActivityEntryEditBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityEntryEditBinding.inflate(layoutInflater);
    }
}