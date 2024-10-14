package com.faltenreich.diaguard.export.pdf

data class PdfPage(
    val header: PdfHeader,
    val footer: PdfFooter,
) {

    class Builder {

        private lateinit var header: PdfHeader
        private lateinit var footer: PdfFooter

        fun header(init: PdfHeader.Builder.() -> Unit) {
            header = PdfHeader.Builder().apply(init).build()
        }

        fun footer(init: PdfFooter.Builder.() -> Unit) {
            footer = PdfFooter.Builder().apply(init).build()
        }

        fun build(): PdfPage {
            return PdfPage(header, footer)
        }
    }
}