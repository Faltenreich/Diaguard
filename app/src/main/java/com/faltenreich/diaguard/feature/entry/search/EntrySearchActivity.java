package com.faltenreich.diaguard.feature.entry.search;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityEntrySearchBinding;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.data.primitive.Vector2D;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class EntrySearchActivity extends BaseActivity<ActivityEntrySearchBinding> {

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

    public EntrySearchActivity() {
        super(R.layout.activity_entry_search);
    }

    @Override
    protected ActivityEntrySearchBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityEntrySearchBinding.inflate(layoutInflater);
    }
}