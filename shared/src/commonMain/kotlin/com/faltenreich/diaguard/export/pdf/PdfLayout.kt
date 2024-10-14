package com.faltenreich.diaguard.export.pdf

import diaguard.shared.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class PdfLayout(
    val title: StringResource,
) {

    LOG(Res.string.log),
    TABLE(Res.string.table),
    TIMELINE(Res.string.timeline),
}