package com.faltenreich.diaguard.shared.view.picker;

import androidx.fragment.app.FragmentManager;

import java.math.BigInteger;

public interface NumberPicking {

    void show(FragmentManager fragmentManager);

    interface Listener {

        void onNumberPicked(BigInteger number);
    }
}
