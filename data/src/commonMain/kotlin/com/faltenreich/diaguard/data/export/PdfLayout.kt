package com.faltenreich.diaguard.data.export

import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.log
import com.faltenreich.diaguard.resource.table
import com.faltenreich.diaguard.resource.timeline
import org.jetbrains.compose.resources.StringResource

enum class PdfLayout(
    val stableId: Int,
    val title: StringResource,
) {

    LOG(
        stableId = 0,
        title = Res.string.log,
    ),
    TABLE(
        stableId = 1,
        title = Res.string.table,
    ),
    TIMELINE(
        stableId = 2,
        title = Res.string.timeline,
    ),
}