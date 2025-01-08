package com.faltenreich.diaguard.shared.wizard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.completed
import diaguard.shared.generated.resources.ic_check
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WizardStepListItem(
    index: Int,
    label: String,
    state: WizardStepState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val alpha =
        if (state == WizardStepState.UPCOMING) AppTheme.dimensions.alpha.SemiTransparent
        else AppTheme.dimensions.alpha.Visible

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(AppTheme.dimensions.size.ImageLarge)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text((index + 1).toString())
        }

        Text(label)

        Spacer(modifier = Modifier.weight(1f))

        when (state) {
            WizardStepState.UPCOMING -> Unit
            WizardStepState.CURRENT -> content()
            WizardStepState.COMPLETED -> Icon(
                painter = painterResource(Res.drawable.ic_check),
                contentDescription = stringResource(Res.string.completed),
                tint = AppTheme.colors.Green,
            )
        }
    }
}