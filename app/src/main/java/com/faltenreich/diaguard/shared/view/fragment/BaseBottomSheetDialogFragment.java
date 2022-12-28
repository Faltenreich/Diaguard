package com.faltenreich.diaguard.shared.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.faltenreich.diaguard.shared.view.ViewBindable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public abstract class BaseBottomSheetDialogFragment<BINDING extends ViewBinding>
    extends BottomSheetDialogFragment
    implements ViewBindable<BINDING>
{

    private BINDING binding;

    public BINDING getBinding() {
        return binding;
    }

    protected abstract BINDING createBinding(LayoutInflater inflater);

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = createBinding(inflater);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
