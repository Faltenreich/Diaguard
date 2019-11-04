package com.faltenreich.diaguard.export.pdf.view;

import android.util.Log;

import com.pdfjet.Cell;
import com.pdfjet.Table;

import java.util.List;

public class SizedTablePdfView extends Table {

    private static final String TAG = SizedTablePdfView.class.getSimpleName();

    private int rowCount;

    public SizedTablePdfView() {
        super();
    }

    public int getRowCount() {
        return rowCount;
    }

    @Override
    public void setData(List<List<Cell>> tableData) throws Exception {
        super.setData(tableData);
        rowCount = tableData.size();
    }

    public float getHeight() {
        float height = 0;
        for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
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
