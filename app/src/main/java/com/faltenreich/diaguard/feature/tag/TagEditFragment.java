package com.faltenreich.diaguard.feature.tag;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.DialogTagEditBinding;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.TagSavedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.dialog.DialogButton;
import com.faltenreich.diaguard.shared.view.dialog.DialogConfig;
import com.faltenreich.diaguard.shared.view.fragment.BaseDialogFragment;

public class TagEditFragment extends BaseDialogFragment<DialogTagEditBinding> {

    @Override
    protected DialogTagEditBinding createBinding(View view) {
        return DialogTagEditBinding.bind(view);
    }

    @Override
    protected DialogConfig getConfig() {
        return new DialogConfig(
            R.layout.dialog_tag_edit,
            requireContext().getString(R.string.tag_new),
            null,
            new DialogButton(android.R.string.ok, this::store),
            new DialogButton(android.R.string.cancel, this::dismiss),
            null
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        ViewUtils.showKeyboard(getBinding().input);
    }

    private void store() {
        String name = getBinding().input.getText().toString();
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<TagResult>() {
            @Override
            public TagResult onShouldLoad(Context context) {
                if (StringUtils.isBlank(name)) {
                    return new TagResult(null, Error.EMPTY);
                } else if (TagDao.getInstance().getByName(name) != null) {
                    return new TagResult(null, Error.DUPLICATE);
                } else {
                    Tag tag = new Tag();
                    tag.setName(name);
                    TagDao.getInstance().createOrUpdate(tag);
                    return new TagResult(tag, null);
                }
            }
            @Override
            public void onDidLoad(TagResult result) {
                if (result.tag != null) {
                    Events.post(new TagSavedEvent(result.tag));
                    dismiss();
                } else {
                    String error = getString(result.error != null
                        ? result.error.textResId
                        : R.string.error_unexpected);
                    getBinding().input.setError(error);
                }
            }
        });
    }

    private enum Error {
        EMPTY(R.string.validator_value_empty),
        DUPLICATE(R.string.tag_duplicate);

        @StringRes private final int textResId;

        Error(@StringRes int textResId) {
            this.textResId = textResId;
        }
    }

    private static class TagResult {
        @Nullable private final Tag tag;
        @Nullable private final Error error;

        private TagResult(@Nullable Tag tag, @Nullable Error error) {
            this.tag = tag;
            this.error = error;
        }
    }
}
