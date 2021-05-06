package com.faltenreich.diaguard.feature.entry.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityEntryEditBinding;
import com.faltenreich.diaguard.feature.navigation.MainButton;
import com.faltenreich.diaguard.feature.navigation.MainButtonProperties;
import com.faltenreich.diaguard.feature.navigation.ToolbarManager;
import com.faltenreich.diaguard.feature.navigation.ToolbarOwner;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;
import com.github.clans.fab.FloatingActionButton;

public class EntryEditActivity
    extends BaseActivity<ActivityEntryEditBinding>
    implements ToolbarOwner, MainButton
{

    private EntryEditFragment fragment;
    private FloatingActionButton fab;

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
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.confirmButton((view) -> fragment.trySubmit(), false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
        initLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void bindViews() {
        fragment = (EntryEditFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fab = getBinding().fabContainer.fab;
    }
    
    private void initLayout() {
        ToolbarManager.applyToolbar(this, getToolbar());
        setTitle(R.string.entry_new);
        fab.setImageResource(getMainButtonProperties().getIconDrawableResId());
        fab.setOnClickListener(getMainButtonProperties().getOnClickListener());
    }
}