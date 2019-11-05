package com.faltenreich.diaguard.export.pdf.view;

import android.util.Log;

import com.pdfjet.Cell;
import com.pdfjet.Table;

import java.util.List;

public class SizedTable extends Table {

    private static final String TAG = SizedTable.class.getSimpleName();

    private int rowCount;

    public SizedTable() {
        super();
    }

    @Override
    public void setData(List<List<Cell>> tableData) throws Exception {
        super.setData(tableData);
        rowCount = tableData.size();
    }

    public float getHeight() {
        float height = 0;
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            try {
                float maxHeightOfRow = 0f;
                for (Cell cell : getRowAtIndex(rowIndex)) {
                    float cellHeight = cell.getHeight();
                    if (cellHeight > maxHeightOfRow) {
                        maxHeightOfRow = cellHeight;
                    }
                }
                height += maxHeightOfRow;
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }
        }
        return height;
    }
}
