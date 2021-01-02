package com.faltenreich.diaguard.feature.entry.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.faltenreich.diaguard.databinding.ActivityEntryEditBinding;
import com.faltenreich.diaguard.feature.navigation.ToolbarManager;
import com.faltenreich.diaguard.feature.navigation.ToolbarOwner;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class EntryEditActivity extends BaseActivity<ActivityEntryEditBinding> implements ToolbarOwner {

    @Override
    protected ActivityEntryEditBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityEntryEditBinding.inflate(layoutInflater);
    }

    @Override
    public Toolbar getToolbar() {
        return getBinding().toolbarContainer.toolbar;
    }

    @Override
    public TextView getTitleView() {
        return getBinding().toolbarContainer.toolbarTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarManager.applyToolbar(this, getToolbar());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}