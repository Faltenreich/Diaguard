package com.faltenreich.diaguard.food.search.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodListLoadingIndicator(modifier: Modifier = Modifier) {
    LinearProgressIndicator(modifier.fillMaxWidth())
}

@Preview
@Composable
private fun Preview() = AppPreview {
    FoodListLoadingIndicator()
}