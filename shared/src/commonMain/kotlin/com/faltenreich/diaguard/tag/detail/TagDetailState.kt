package com.faltenreich.diaguard.tag.detail

data class TagDetailState(
    val deleteDialog: DeleteDialog?,
) {

    data object DeleteDialog
}