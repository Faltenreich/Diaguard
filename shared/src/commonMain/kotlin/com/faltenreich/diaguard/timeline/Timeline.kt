package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvas
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_chevron_back
import diaguard.shared.generated.resources.ic_chevron_forward
import diaguard.shared.generated.resources.timeline
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    viewModel: TimelineViewModel = inject(),
) {
    val state = viewModel.collectState() ?: return

    Column(modifier = modifier) {
        TimelineCanvas(
            state = state,
            viewModel = viewModel,
            modifier = Modifier.weight(1f),
        )
        Row(
            modifier = Modifier
                .height(AppTheme.dimensions.size.TouchSizeLarge)
                .background(AppTheme.colors.scheme.primaryContainer),
        ) {
            IconButton(
                onClick = { viewModel.dispatchIntent(TimelineIntent.SelectPreviousDay) },
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(all = AppTheme.dimensions.padding.P_2),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_chevron_back),
                    contentDescription = stringResource(Res.string.timeline),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = { viewModel.dispatchIntent(TimelineIntent.SelectDate) },
                modifier = Modifier.fillMaxHeight(),
            ) {
                Text(
                    text = state.currentDate.toString(),
                    color = AppTheme.colors.scheme.onPrimary,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { viewModel.dispatchIntent(TimelineIntent.SelectNextDay) },
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(all = AppTheme.dimensions.padding.P_2),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_chevron_forward),
                    contentDescription = stringResource(Res.string.timeline),
                )
            }
        }
    }
}