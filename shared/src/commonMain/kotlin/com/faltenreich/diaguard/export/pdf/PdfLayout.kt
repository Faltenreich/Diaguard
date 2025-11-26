package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.log
import com.faltenreich.diaguard.resource.table
import com.faltenreich.diaguard.resource.timeline
import org.jetbrains.compose.resources.StringResource

enum class PdfLayout(
    val title: StringResource,
) {

    LOG(Res.string.log),
    TABLE(Res.string.table),
    TIMELINE(Res.string.timeline),
}