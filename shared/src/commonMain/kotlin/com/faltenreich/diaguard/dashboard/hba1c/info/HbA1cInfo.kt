package com.faltenreich.diaguard.dashboard.hba1c.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.estimated
import diaguard.shared.generated.resources.hba1c
import diaguard.shared.generated.resources.hba1c_estimated_description
import diaguard.shared.generated.resources.latest
import diaguard.shared.generated.resources.placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun HbA1cInfo(
    viewModel: HbA1cInfoViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = getString(Res.string.hba1c),
            style = AppTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        Text(
            text = getString(Res.string.latest),
            style = AppTheme.typography.titleMedium,
        )

        Text(
            text = state?.latest?.value ?: getString(Res.string.placeholder),
            style = AppTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        Text(
            text = getString(Res.string.estimated),
            style = AppTheme.typography.titleMedium,
        )

        Text(
            text = state?.estimated?.value ?: getString(Res.string.placeholder),
            style = AppTheme.typography.bodyLarge,
        )

        state?.estimated?.let { estimated ->
            Text(
                stringResource(
                    Res.string.hba1c_estimated_description,
                    estimated.valueCount.toString(),
                    estimated.dateTimeRangeStart,
                )
            )
        }
    }
}