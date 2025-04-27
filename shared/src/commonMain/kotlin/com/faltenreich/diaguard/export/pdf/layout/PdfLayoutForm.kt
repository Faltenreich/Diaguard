package com.faltenreich.diaguard.export.pdf.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.layout

@Composable
fun PdfLayoutForm(
    selection: PdfLayout,
    onChange: (PdfLayout) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = getString(Res.string.layout),
            style = AppTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        Column(modifier = Modifier.selectableGroup()) {
            PdfLayout.entries.forEach { pdfLayout ->
                PdfLayoutListItem(
                    pdfLayout = pdfLayout,
                    isSelected = selection == pdfLayout,
                    onClick = { onChange(pdfLayout) },
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))
    }
}