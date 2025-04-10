package com.faltenreich.diaguard.dashboard.hba1c.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c_estimated
import diaguard.shared.generated.resources.hba1c_estimated_description
import diaguard.shared.generated.resources.hba1c_estimated_formula

@Composable
fun EstimatedHbA1cInfo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.dimensions.padding.P_3),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3_5),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = getString(Res.string.hba1c_estimated),
            style = AppTheme.typography.titleLarge,
        )

        Text(
            text = getString(Res.string.hba1c_estimated_formula),
            fontStyle = FontStyle.Italic,
        )

        Text(
            text = getString(Res.string.hba1c_estimated_description),
            textAlign = TextAlign.Justify,
        )
    }
}