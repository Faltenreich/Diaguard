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
import com.faltenreich.diaguard.shared.view.reveal.Revealable;

public class EntrySearchActivity extends BaseActivity<ActivityEntrySearchBinding> implements Navigating {

    private static final String ARGUMENT_REVEAL_X = "revealX";
    private static final String ARGUMENT_REVEAL_Y = "revealY";
    private static final String ARGUMENT_TAG_ID = "tagId";

    public static Intent newInstance(Context context, @Nullable View source) {
        Intent intent = new Intent(context, EntrySearchActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && source != null) {
            Vector2D position = ViewUtils.getPositionOnScreen(source);
            intent.putExtra(ARGUMENT_REVEAL_X, position.x + (source.getWidth() / 2));
            intent.putExtra(ARGUMENT_REVEAL_Y, position.y + (source.getHeight() / 2));
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        return intent;
    }

    public static Intent newInstance(Context context, Tag tag) {
        Intent intent = new Intent(context, EntrySearchActivity.class);
        intent.putExtra(ARGUMENT_TAG_ID, tag.getId());
        return intent;
    }

    private int revealX;
    private int revealY;
    private long tagId;

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
        Intent arguments = getIntent();
        revealX = arguments.getIntExtra(ARGUMENT_REVEAL_X, -1);
        revealY = arguments.getIntExtra(ARGUMENT_REVEAL_Y, -1);
        tagId = arguments.getLongExtra(ARGUMENT_TAG_ID, -1);
    }

    private void addFragment() {
        Fragment fragment = EntrySearchFragment.newInstance(revealX, revealY, tagId);
        openFragment(fragment, Navigation.Operation.REPLACE, false);
    }

    @Override
    public void openFragment(@NonNull Fragment fragment, @NonNull Navigation.Operation operation, boolean addToBackStack) {
        Navigation.openFragment(fragment, getSupportFragmentManager(), R.id.fragment_container, operation, addToBackStack);
    }

    @Override
    public void finish() {
        Fragment fragment = Navigation.getCurrentFragment(getSupportFragmentManager(), R.id.fragment_container);
        if (fragment instanceof Revealable) {
            ((Revealable) fragment).unreveal(() -> {
                EntrySearchActivity.super.finish();
                overridePendingTransition(0, 0);
            });
        } else {
            super.finish();
        }
    }
}