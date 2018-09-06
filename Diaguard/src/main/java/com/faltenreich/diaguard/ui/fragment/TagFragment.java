package com.faltenreich.diaguard.ui.fragment;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.EditText;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.async.DataLoader;
import com.faltenreich.diaguard.data.async.DataLoaderListener;
import com.faltenreich.diaguard.data.dao.TagDao;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.ui.view.DialogButton;
import com.faltenreich.diaguard.util.StringUtils;
import com.faltenreich.diaguard.util.SystemUtils;

import butterknife.BindView;

public class TagFragment extends BaseDialogFragment {

    @BindView(R.id.input) EditText editText;

    public TagFragment() {
        super(R.string.tag_new, R.layout.dialog_input);
    }

    private void trySubmit() {
        final String name = editText.getText().toString();
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<TagError>() {
            @Override
            public TagError onShouldLoad() {
                if (StringUtils.isBlank(name)) {
                    return TagError.EMPTY;
                } else if (TagDao.getInstance().getByName(name) != null) {
                    return TagError.DUPLICATE;
                } else {
                    Tag tag = new Tag();
                    tag.setName(name);
                    TagDao.getInstance().createOrUpdate(tag);
                    return null;
                }
            }
            @Override
            public void onDidLoad(TagError tagError) {
                if (tagError == null) {
                    SystemUtils.hideKeyboard(getActivity());
                    dismiss();
                } else {
                    editText.setError(getString(tagError.textResId));
                }
            }
        });
    }

    @Nullable
    @Override
    protected DialogButton createPositiveButton() {
        return new DialogButton(android.R.string.ok, new DialogButton.DialogButtonListener() {
            @Override
            public void onClick() {
                trySubmit();
            }
        });
    }

    private enum TagError {
        EMPTY(R.string.validator_value_empty),
        DUPLICATE(R.string.tag_duplicate);

        @StringRes private int textResId;

        TagError(@StringRes int textResId) {
            this.textResId = textResId;
        }
    }
}
