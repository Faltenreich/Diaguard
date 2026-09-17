package com.faltenreich.diaguard.export.pdf

@Suppress("MagicNumber")
internal enum class PdfSpacing(val points: Float) {
    P_4(4f),
    P_8(8f),
    P_24(24f),
    P_32(32f),
    ;

    companion object {

        val CELL_PADDING = P_4
        val HEADER_PADDING_BOTTOM = P_32
        val DAY_PADDING_BOTTOM = P_24
    }
}