package com.faltenreich.diaguard.export.pdf.layout

import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.log
import diaguard.shared.generated.resources.table
import diaguard.shared.generated.resources.timeline
import org.jetbrains.compose.resources.StringResource

enum class PdfLayout(
    val titleResource: StringResource,
) {

    LOG(
        titleResource = Res.string.log,
    ),
    TABLE(
        titleResource = Res.string.table,
    ),
    TIMELINE(
        titleResource = Res.string.timeline,
    ),
}