package com.faltenreich.diaguard.data.export

import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.csv
import com.faltenreich.diaguard.resource.pdf
import com.faltenreich.diaguard.view.theme.color.Colors
import org.jetbrains.compose.resources.StringResource

enum class ExportType(
    val stableId: Int,
    val title: StringResource,
    val color: Color,
    val extension: String,
) {

    CSV(
        stableId = 0,
        extension = "csv",
        title = Res.string.csv,
        color = Colors.GreenDark,
    ),
    PDF(
        stableId = 1,
        extension = "pdf",
        title = Res.string.pdf,
        color = Colors.RedDark,
    ),
}