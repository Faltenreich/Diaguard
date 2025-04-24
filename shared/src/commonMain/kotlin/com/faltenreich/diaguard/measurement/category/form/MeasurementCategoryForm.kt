package com.faltenreich.diaguard.measurement.category.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyList
import com.faltenreich.diaguard.preference.color.isDark
import com.faltenreich.diaguard.shared.architecture.collectAsStateWithLifecycle
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DeleteDialog
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.EmojiPicker
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextCheckbox
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.delete_error_pre_defined
import diaguard.shared.generated.resources.delete_title
import diaguard.shared.generated.resources.icon
import diaguard.shared.generated.resources.measurement_category_visibility
import diaguard.shared.generated.resources.measurement_category_visibility_hidden
import diaguard.shared.generated.resources.measurement_category_visibility_visible
import diaguard.shared.generated.resources.name
import diaguard.shared.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
fun MeasurementCategoryForm(
    viewModel: MeasurementCategoryFormViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState()
    val name = viewModel.name.collectAsStateWithLifecycle().value
    val icon = viewModel.icon.collectAsStateWithLifecycle().value
    val isActive = viewModel.isActive.collectAsStateWithLifecycle().value

    var showEmojiPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        TextInput(
            input = name,
            onInputChange = { viewModel.name.value = it },
            label = getString(Res.string.name),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppTheme.dimensions.padding.P_0,
                    top = AppTheme.dimensions.padding.P_3,
                    end = AppTheme.dimensions.padding.P_0,
                    bottom = AppTheme.dimensions.padding.P_0,
                ),
        )

        Divider()

        FormRow(
            modifier = Modifier
                .clickable { showEmojiPicker = true }
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(Res.string.icon),
                modifier = Modifier.weight(1f),
            )
            MeasurementCategoryIcon(
                icon = icon,
                fallback = name,
            )
        }

        Divider()

        FormRow {
            TextCheckbox(
                title = stringResource(Res.string.measurement_category_visibility),
                subtitle = stringResource(
                    if (isActive) Res.string.measurement_category_visibility_visible
                    else Res.string.measurement_category_visibility_hidden
                ),
                checked = isActive,
                onCheckedChange = { viewModel.isActive.value = it },
            )
        }

        AnimatedVisibility(
            visible = state != null,
            enter = fadeIn(),
        ) {
            MeasurementPropertyList(properties = state?.properties ?: emptyList())
        }
    }

    if (showEmojiPicker) {
        val sheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            onDismissRequest = { showEmojiPicker = false },
            sheetState = sheetState,
        ) {
            EmojiPicker(
                onEmojiPicked = { icon ->
                    showEmojiPicker = false
                    viewModel.icon.value = icon
                },
                // TODO: Adjust for smaller/larger screens
                columns = 9,
                isDarkColorScheme = state?.colorScheme.isDark(),
                // Workaround: Fixes nested scroll
                // FIXME: Lags after expanding bottom sheet
                modifier = if (sheetState.currentValue == SheetValue.Expanded) {
                    Modifier.verticalScroll(rememberScrollState())
                } else {
                    Modifier
                }
            )
        }
    }

    if (state?.deleteDialog != null) {
        DeleteDialog(
            onDismissRequest = { viewModel.dispatchIntent(MeasurementCategoryFormIntent.CloseDeleteDialog) },
            onConfirmRequest = {
                viewModel.dispatchIntent(MeasurementCategoryFormIntent.CloseDeleteDialog)
                viewModel.dispatchIntent(MeasurementCategoryFormIntent.Delete(needsConfirmation = false))
            },
        )
    }

    if (state?.alertDialog != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dispatchIntent(MeasurementCategoryFormIntent.CloseAlertDialog) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.dispatchIntent(MeasurementCategoryFormIntent.CloseAlertDialog) },
                ) {
                    Text(
                        text = getString(Res.string.ok),
                        color = AppTheme.colors.scheme.onBackground,
                    )
                }
            },
            title = { Text(stringResource(Res.string.delete_title)) },
            text = { Text(stringResource(Res.string.delete_error_pre_defined)) },
        )
    }
}