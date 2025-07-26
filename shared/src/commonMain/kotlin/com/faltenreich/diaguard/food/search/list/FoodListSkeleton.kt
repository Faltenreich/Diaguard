package com.faltenreich.diaguard.food.search.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.shared.view.skeleton
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodListSkeleton(modifier: Modifier = Modifier) {
    LazyColumn(modifier) {
        items(count = 20) {
            Column {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .defaultMinSize(minHeight = AppTheme.dimensions.size.TouchSizeLarge)
                        .padding(AppTheme.dimensions.padding.P_3),
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(1f).skeleton(true))
                    Box(modifier = Modifier.width(50.dp).skeleton(true))
                }
                Divider()
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    FoodListSkeleton()
}