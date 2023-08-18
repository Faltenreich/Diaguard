package com.faltenreich.diaguard.dashboard.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun LatestDashboardItem(
    data: DashboardViewState.Revisit.BloodSugar?,
    modifier: Modifier = Modifier,
    dateTimeFormatter: DateTimeFormatter = inject(),
) {
    val navigator = LocalNavigator.currentOrThrow
    Card(
        onClick = { navigator.push(Screen.EntryForm(entry = data?.value?.entry)) },
        modifier = modifier,
    ) {
        when (data) {
            null -> Text(
                text = stringResource(MR.strings.entry_first_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = AppTheme.dimensions.padding.P_3),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.headlineMedium,
            )
            else -> Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = AppTheme.dimensions.padding.P_2),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = data.value.value.toString(), // TODO: Format
                    style = AppTheme.typography.headlineLarge,
                )
                Row {
                    Text(dateTimeFormatter.formatDateTime(data.value.entry.dateTime))
                    Text(" - ")
                    Text(dateTimeFormatter.formatTimePassed(data.value.entry.dateTime))
                }
            }
        }
    }
}