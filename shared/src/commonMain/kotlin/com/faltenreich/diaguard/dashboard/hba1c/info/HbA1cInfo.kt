package com.faltenreich.diaguard.dashboard.hba1c.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextDivider
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c_estimated_description
import diaguard.shared.generated.resources.hba1c_estimation
import diaguard.shared.generated.resources.hba1c_latest_entries
import diaguard.shared.generated.resources.placeholder

@Composable
fun HbA1cInfo(
    viewModel: HbA1cInfoViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextDivider(getString(Res.string.hba1c_estimation))

        Column(modifier = Modifier.padding(AppTheme.dimensions.padding.P_3)) {
            Text(
                text = state?.estimated?.value ?: getString(Res.string.placeholder),
                style = AppTheme.typography.bodyLarge,
            )
            Text(
                text = getString(Res.string.hba1c_estimated_description),
                textAlign = TextAlign.Justify,
                style = AppTheme.typography.bodyLarge,
            )
        }

        TextDivider(getString(Res.string.hba1c_latest_entries))

        Column(modifier = Modifier.padding(AppTheme.dimensions.padding.P_3)) {
            Text(
                text = state?.latest?.value ?: getString(Res.string.placeholder),
                style = AppTheme.typography.bodyLarge,
            )
        }
    }
}