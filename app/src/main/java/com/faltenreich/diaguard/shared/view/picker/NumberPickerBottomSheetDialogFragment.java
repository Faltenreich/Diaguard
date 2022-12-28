package com.faltenreich.diaguard.shared.view.picker;

import android.view.LayoutInflater;

import androidx.fragment.app.FragmentManager;

import com.faltenreich.diaguard.databinding.DialogFootInputAmountBinding;
import com.faltenreich.diaguard.shared.view.fragment.BaseBottomSheetDialogFragment;

public class NumberPickerBottomSheetDialogFragment
    extends BaseBottomSheetDialogFragment<DialogFootInputAmountBinding>
    implements NumberPicking
{

    private final static String TAG = NumberPickerBottomSheetDialogFragment.class.getSimpleName();

    private String title;
    private int initialValue;
    private int minValue;
    private int maxValue;
    private Listener listener;

    static NumberPickerBottomSheetDialogFragment newInstance(
        String title,
        int initialValue,
        int minValue,
        int maxValue,
        Listener listener
    ) {
        return new NumberPickerBottomSheetDialogFragment();
    }

    @Override
    protected DialogFootInputAmountBinding createBinding(LayoutInflater inflater) {
        return DialogFootInputAmountBinding.inflate(inflater);
    }

    @Override
    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }
}
