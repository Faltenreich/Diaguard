package com.faltenreich.diaguard.feature.tag;

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
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.dialog.DialogButton;
import com.faltenreich.diaguard.shared.view.dialog.DialogConfig;
import com.faltenreich.diaguard.shared.view.fragment.BaseDialogFragment;

public class TagEditFragment extends BaseDialogFragment<DialogTagEditBinding> {

    // FIXME: Replace with event to prevent memory leaks
    private TagListener listener;

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
            new DialogButton(android.R.string.ok, this::trySubmit),
            new DialogButton(android.R.string.cancel, this::dismiss),
            null
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        ViewUtils.showKeyboard(getBinding().input);
    }

    public void setListener(TagListener listener) {
        this.listener = listener;
    }

    private void trySubmit() {
        String name = getBinding().input.getText().toString();
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<TagResult>() {
            @Override
            public TagResult onShouldLoad() {
                Tag tag = null;
                TagError error = findError(name);
                if (error == null) {
                    tag = createTag(name);
                }
                return new TagResult(tag, error);
            }
            @Override
            public void onDidLoad(TagResult result) {
                if (listener != null) {
                    listener.onResult(result.tag);
                }
                if (result.tag != null) {
                    dismiss();
                } else {
                    getBinding().input.setError(getString(result.error != null ? result.error.textResId : R.string.error_unexpected));
                }
            }
        });
    }

    @Nullable
    private TagError findError(String name) {
        if (StringUtils.isBlank(name)) {
            return TagError.EMPTY;
        } else if (TagDao.getInstance().getByName(name) != null) {
            return TagError.DUPLICATE;
        } else {
            return null;
        }
    }

    private Tag createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        TagDao.getInstance().createOrUpdate(tag);
        return tag;
    }

    private enum TagError {
        EMPTY(R.string.validator_value_empty),
        DUPLICATE(R.string.tag_duplicate);

        @StringRes private final int textResId;

        TagError(@StringRes int textResId) {
            this.textResId = textResId;
        }
    }

    private static class TagResult {
        @Nullable private final Tag tag;
        @Nullable private final TagError error;

        private TagResult(@Nullable Tag tag, @Nullable TagError error) {
            this.tag = tag;
            this.error = error;
        }
    }

    public interface TagListener {
        void onResult(@Nullable Tag tag);
    }
}
