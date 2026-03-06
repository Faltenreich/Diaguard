package com.faltenreich.diaguard.persistence.file

import com.faltenreich.diaguard.datetime.DateTime

data class File(
    val absolutePath: String,
    val createdAt: DateTime?,
    val mimeType: String?,
)