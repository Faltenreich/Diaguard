package com.faltenreich.diaguard.ui.fragment;

import android.support.annotation.Nullable;
import android.widget.EditText;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.async.DataLoader;
import com.faltenreich.diaguard.data.async.DataLoaderListener;
import com.faltenreich.diaguard.data.dao.TagDao;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.ui.view.DialogButton;
import com.faltenreich.diaguard.util.SystemUtils;

import butterknife.BindView;

public class TagFragment extends BaseDialogFragment {

    @BindView(R.id.input) EditText editText;

    public TagFragment() {
        super(R.string.tag_new, R.layout.dialog_input);
    }

    private void trySubmit() {
        final String name = editText.getText().toString();
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<Boolean>() {
            @Override
            public Boolean onShouldLoad() {
                // TODO: Check for empty and duplicate tags
                boolean isValid = false;
                if (isValid) {
                    Tag tag = new Tag();
                    tag.setName(name);
                    TagDao.getInstance().createOrUpdate(tag);
                }
                return isValid;
            }
            @Override
            public void onDidLoad(Boolean isValid) {
                if (isValid) {
                    // TODO
                    SystemUtils.hideKeyboard(getActivity());
                } else {
                    // TODO: Do not dismiss dialog
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
}
