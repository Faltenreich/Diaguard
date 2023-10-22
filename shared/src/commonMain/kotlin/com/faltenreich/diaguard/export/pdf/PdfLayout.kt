package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

enum class PdfLayout(
    val title: StringResource,
) {

    LOG(MR.strings.log),
    TABLE(MR.strings.table),
    TIMELINE(MR.strings.timeline),
}