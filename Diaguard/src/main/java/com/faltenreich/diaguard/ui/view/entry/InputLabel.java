package com.faltenreich.diaguard.ui.view.entry;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Faltenreich on 27.10.2016.
 */

public class InputLabel {

    private EditText input;
    private TextView label;

    public InputLabel(EditText input, TextView label) {
        this.input = input;
        this.label = label;
    }

    public EditText getInput() {
        return input;
    }

    public TextView getLabel() {
        return label;
    }
}
