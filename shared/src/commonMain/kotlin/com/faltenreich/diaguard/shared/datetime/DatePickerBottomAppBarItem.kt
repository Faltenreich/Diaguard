package com.faltenreich.diaguard.shared.datetime

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem

@Composable
fun DatePickerBottomAppBarItem() {
    val navigator = LocalNavigator.currentOrThrow
    BottomAppBarItem(
        image = Icons.Filled.DateRange,
        contentDescription = MR.strings.date_pick,
        onClick = { },
    )
}