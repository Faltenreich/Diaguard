package com.faltenreich.diaguard.data.export

import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.csv
import com.faltenreich.diaguard.resource.pdf
import org.jetbrains.compose.resources.StringResource

enum class ExportType(
    val stableId: Int,
    val title: StringResource,
) {

    CSV(
        stableId = 0,
        title = Res.string.csv,
    ),
    PDF(
        stableId = 1,
        title = Res.string.pdf,
    ),
}