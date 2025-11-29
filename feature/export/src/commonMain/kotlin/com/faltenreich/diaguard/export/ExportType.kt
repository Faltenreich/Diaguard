package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.csv
import com.faltenreich.diaguard.resource.pdf
import org.jetbrains.compose.resources.StringResource

enum class ExportType(
    val title: StringResource,
) {

    CSV(Res.string.csv),
    PDF(Res.string.pdf),
}