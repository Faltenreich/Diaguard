package com.faltenreich.diaguard.feature.tag;

import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.dialog.DialogButton;
import com.faltenreich.diaguard.shared.view.fragment.BaseDialogFragment;

import butterknife.BindView;

public class TagEditFragment extends BaseDialogFragment {

    @BindView(R.id.input) EditText editText;

    // FIXME: Replace with event to prevent memory leaks
    private TagListener listener;

    public TagEditFragment() {
        super(R.string.tag_new, R.layout.dialog_input);
    }

    @Override
    public void onStart() {
        super.onStart();
        ViewUtils.showKeyboard(editText);
    }

    public void setListener(TagListener listener) {
        this.listener = listener;
    }

    private void trySubmit() {
        final String name = editText.getText().toString();
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
                    editText.setError(getString(result.error != null ? result.error.textResId : R.string.error_unexpected));
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

    @Nullable
    @Override
    protected DialogButton createNegativeButton() {
        return new DialogButton(android.R.string.cancel, this::dismiss);
    }

    @Nullable
    @Override
    protected DialogButton createPositiveButton() {
        return new DialogButton(android.R.string.ok, this::trySubmit);
    }

    private enum TagError {
        EMPTY(R.string.validator_value_empty),
        DUPLICATE(R.string.tag_duplicate);

        @StringRes private int textResId;

        TagError(@StringRes int textResId) {
            this.textResId = textResId;
        }
    }

    private static class TagResult {
        @Nullable private Tag tag;
        @Nullable private TagError error;

        private TagResult(@Nullable Tag tag, @Nullable TagError error) {
            this.tag = tag;
            this.error = error;
        }
    }

    public interface TagListener {
        void onResult(@Nullable Tag tag);
    }
}
