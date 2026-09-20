package com.faltenreich.diaguard.persistence.pdf

data class PdfSize(
    val width: Float,
    val height: Float,
) {

    companion object {

        val Zero = PdfSize(0f, 0f)
    }
}