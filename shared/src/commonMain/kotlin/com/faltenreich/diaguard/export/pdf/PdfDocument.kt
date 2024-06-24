package com.faltenreich.diaguard.export.pdf

data class PdfDocument(val pages: List<PdfPage>) {

    class Builder {

        private val pages = mutableListOf<PdfPage>()

        fun page(init: PdfPage.Builder.() -> Unit) {
            pages += PdfPage.Builder().apply(init).build()
        }

        fun build(): PdfDocument {
            return PdfDocument(pages)
        }
    }
}

fun document(init: PdfDocument.Builder.() -> Unit): PdfDocument {
    return PdfDocument.Builder().apply(init).build()
}