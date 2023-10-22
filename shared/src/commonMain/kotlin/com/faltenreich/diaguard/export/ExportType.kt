package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

enum class ExportType(
    val title: StringResource,
) {

    CSV(MR.strings.csv),
    PDF(MR.strings.pdf),
}