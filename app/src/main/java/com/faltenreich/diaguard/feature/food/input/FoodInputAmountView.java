package com.faltenreich.diaguard.feature.food.input;

import android.view.LayoutInflater;

import com.faltenreich.diaguard.databinding.DialogFootInputAmountBinding;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.view.fragment.BaseBottomSheetDialogFragment;

public class FoodInputAmountView extends BaseBottomSheetDialogFragment<DialogFootInputAmountBinding> {

    public static String TAG = FoodInputAmountView.class.getSimpleName();

    public static FoodInputAmountView newInstance(FoodEaten foodEaten) {
        return new FoodInputAmountView();
    }

    @Override
    protected DialogFootInputAmountBinding createBinding(LayoutInflater inflater) {
        return DialogFootInputAmountBinding.inflate(inflater);
    }
}
