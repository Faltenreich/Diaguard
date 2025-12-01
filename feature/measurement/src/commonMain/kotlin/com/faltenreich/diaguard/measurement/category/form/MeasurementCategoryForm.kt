package com.faltenreich.diaguard.measurement.category.form

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
import com.faltenreich.diaguard.data.preference.color.ColorScheme
import com.faltenreich.diaguard.data.preference.color.isDark
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.measurement.category.icon.MeasurementCategoryIcon
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyList
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.delete_error_pre_defined
import com.faltenreich.diaguard.resource.delete_title
import com.faltenreich.diaguard.resource.icon
import com.faltenreich.diaguard.resource.measurement_category_visibility
import com.faltenreich.diaguard.resource.measurement_category_visibility_hidden
import com.faltenreich.diaguard.resource.measurement_category_visibility_visible
import com.faltenreich.diaguard.resource.name
import com.faltenreich.diaguard.resource.ok
import com.faltenreich.diaguard.view.checkbox.TextCheckbox
import com.faltenreich.diaguard.view.divider.Divider
import com.faltenreich.diaguard.view.image.EmojiPicker
import com.faltenreich.diaguard.view.input.TextInput
import com.faltenreich.diaguard.view.layout.FormRow
import com.faltenreich.diaguard.view.overlay.DeleteDialog
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementCategoryForm(
    state: MeasurementCategoryFormState?,
    onIntent: (MeasurementCategoryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    var showEmojiPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        TextInput(
            input = state.name,
            onInputChange = { input ->
                onIntent(MeasurementCategoryFormIntent.SetName(input))
            },
            label = stringResource(Res.string.name),
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
                icon = state.icon,
                name = state.name,
            )
        }

        Divider()

        FormRow {
            TextCheckbox(
                title = stringResource(Res.string.measurement_category_visibility),
                subtitle = stringResource(
                    if (state.isActive) Res.string.measurement_category_visibility_visible
                    else Res.string.measurement_category_visibility_hidden
                ),
                checked = state.isActive,
                onCheckedChange = { isChecked ->
                    onIntent(MeasurementCategoryFormIntent.SetIsActive(isChecked))
                },
            )
        }

        MeasurementPropertyList(
            properties = state.properties,
            onIntent = onIntent,
        )
    }

    if (showEmojiPicker) {
        val sheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            onDismissRequest = { showEmojiPicker = false },
            sheetState = sheetState,
        ) {
            EmojiPicker(
                onEmojiPick = { icon ->
                    showEmojiPicker = false
                    onIntent(MeasurementCategoryFormIntent.SetIcon(icon))
                },
                // TODO: Adjust for smaller/larger screens
                columns = 9,
                isDarkColorScheme = state.colorScheme.isDark(),
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

    if (state.deleteDialog != null) {
        DeleteDialog(
            onDismissRequest = { onIntent(MeasurementCategoryFormIntent.CloseDeleteDialog) },
            onConfirmRequest = {
                onIntent(MeasurementCategoryFormIntent.CloseDeleteDialog)
                onIntent(MeasurementCategoryFormIntent.Delete(needsConfirmation = false))
            },
        )
    }

    if (state.alertDialog != null) {
        AlertDialog(
            onDismissRequest = { onIntent(MeasurementCategoryFormIntent.CloseAlertDialog) },
            confirmButton = {
                TextButton(
                    onClick = { onIntent(MeasurementCategoryFormIntent.CloseAlertDialog) },
                ) {
                    Text(
                        text = stringResource(Res.string.ok),
                        color = AppTheme.colors.scheme.onBackground,
                    )
                }
            },
            title = { Text(stringResource(Res.string.delete_title)) },
            text = { Text(stringResource(Res.string.delete_error_pre_defined)) },
        )
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    val category = category()
    MeasurementCategoryForm(
        state = MeasurementCategoryFormState(
            name = category.name,
            icon = category.icon,
            isActive = category.isActive,
            properties = listOf(property()),
            colorScheme = ColorScheme.SYSTEM,
            deleteDialog = null,
            alertDialog = null,
        ),
        onIntent = {},
    )
}