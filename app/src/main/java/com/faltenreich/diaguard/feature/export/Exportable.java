package com.faltenreich.diaguard.feature.export;

import android.content.Context;

public interface Exportable {
    String[] getValuesForExport(Context context);
}
