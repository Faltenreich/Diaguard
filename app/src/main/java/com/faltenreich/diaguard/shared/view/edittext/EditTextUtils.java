package com.faltenreich.diaguard.shared.view.edittext;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditTextUtils {

    public static void afterTextChanged(EditText editText, OnTextChangedListener listener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable editable) {
                listener.onTextChanged();
            }
        });
    }
}