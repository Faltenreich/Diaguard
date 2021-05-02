package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.faltenreich.diaguard.shared.data.database.entity.Measurement;

import java.util.ArrayList;
import java.util.List;

public class MeasurementSavedState extends View.BaseSavedState {

    private final ArrayList<Measurement> measurements;

    public MeasurementSavedState(Parcelable source, ArrayList<Measurement> measurements) {
        super(source);
        this.measurements = measurements;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeSerializable(measurements);
    }
}
