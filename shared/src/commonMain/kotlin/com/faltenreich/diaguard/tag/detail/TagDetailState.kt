package com.faltenreich.diaguard.tag.detail

data class TagDetailState(
    val name: String,
    val error: String?,
    val deleteDialog: DeleteDialog?,
) {

    data object DeleteDialog
}