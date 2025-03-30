package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.navigation.modal.Modal

class EmojiModal(
    private val onDismissRequest: () -> Unit,
    private val onEmojiPicked: (String) -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
        ) {
            EmojiPicker(
                onEmojiPicked = onEmojiPicked,
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
}