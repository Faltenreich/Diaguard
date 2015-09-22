package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementFacadeView extends CardView {
    
    @Bind(R.id.layout_content)
    protected MeasurementGenericView content;

    public MeasurementFacadeView(Context context) {
        super(context);
        init();
    }

    public MeasurementFacadeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MeasurementFacadeView(Context context, Measurement.Category category) {
        super(context);
        init();
    }
    
    private void init() {
        ButterKnife.bind(this);
    }
    
    public boolean isValid() {
        return content.isValid();
    }
    
}
