package com.faltenreich.diaguard.feature.entry.search;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityEntrySearchBinding;
import com.faltenreich.diaguard.feature.navigation.Navigating;
import com.faltenreich.diaguard.feature.navigation.Navigation;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.data.primitive.Vector2D;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class EntrySearchActivity extends BaseActivity<ActivityEntrySearchBinding> implements Navigating {

    public static void show(Context context, @Nullable View source) {
        Intent intent = new Intent(context, EntrySearchActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && source != null) {
            Vector2D position = ViewUtils.getPositionOnScreen(source);
            intent.putExtra(EntrySearchFragment.ARGUMENT_REVEAL_X, position.x + (source.getWidth() / 2));
            intent.putExtra(EntrySearchFragment.ARGUMENT_REVEAL_Y, position.y + (source.getHeight() / 2));
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        context.startActivity(intent);
    }

    public static void show(Context context, Tag tag) {
        Intent intent = new Intent(context, EntrySearchActivity.class);
        intent.putExtra(EntrySearchFragment.ARGUMENT_TAG_ID, tag.getId());
        context.startActivity(intent);
    }

    private int revealX;
    private int revealY;

    public EntrySearchActivity() {
        super(R.layout.activity_entry_search);
    }

    @Override
    protected ActivityEntrySearchBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityEntrySearchBinding.inflate(layoutInflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArguments();
        addFragment();
    }

    private void getArguments() {
        revealX = getIntent().getIntExtra(EntrySearchFragment.ARGUMENT_REVEAL_X, -1);
        revealY = getIntent().getIntExtra(EntrySearchFragment.ARGUMENT_REVEAL_Y, -1);
    }

    private void addFragment() {
        Fragment fragment = new EntrySearchFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(EntrySearchFragment.ARGUMENT_REVEAL_X, revealX);
        arguments.putInt(EntrySearchFragment.ARGUMENT_REVEAL_Y, revealY);
        fragment.setArguments(arguments);
        openFragment(fragment, Navigation.Operation.REPLACE, false);
    }

    @Override
    public void openFragment(@NonNull Fragment fragment, @NonNull Navigation.Operation operation, boolean addToBackStack) {
        Navigation.openFragment(fragment, getSupportFragmentManager(), R.id.fragment_container, operation, addToBackStack);
    }
}