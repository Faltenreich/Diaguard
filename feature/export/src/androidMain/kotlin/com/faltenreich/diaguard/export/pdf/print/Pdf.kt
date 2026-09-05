package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Point
import android.graphics.PointF
import android.graphics.pdf.PdfDocument
import android.util.Size
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

internal class Pdf {

    private val document = PdfDocument()
    private lateinit var outputStream: OutputStream
    lateinit var page: PdfDocument.Page
    private var offset: PointF = PAGE_PADDING

    fun open(file: File) {
        outputStream = FileOutputStream(file)
    }

    fun close() {
        document.close()
    }

    fun addPage() {
        val pageInfo = PdfDocument.PageInfo.Builder(
            DIN_A4.width,
            DIN_A4.height,
            document.pages.size,
        ).create()
        page = document.startPage(pageInfo)
        offset = PAGE_PADDING
    }

    fun closePage() {
        document.finishPage(page)
        document.writeTo(outputStream)
    }

    fun draw(drawable: PdfDrawable) {
        drawable.drawOn(page.canvas, offset)
    }

    fun move(offset: PointF) {
        // TODO: Check bounds and add page if needed
        this.offset.set(this.offset.x + offset.x, this.offset.y + offset.y)
    }

    fun move(offset: Point) {
        move(PointF(offset.x.toFloat(), offset.y.toFloat()))
    }

    fun moveX(by: Float) {
        move(PointF(by, 0f))
    }

    fun moveX(by: Int) {
        move(PointF(by.toFloat(), 0f))
    }

    fun moveY(by: Float) {
        move(PointF(0f, by))
    }

    fun moveY(by: Int) {
        move(PointF(0f, by.toFloat()))
    }

    private companion object {

        private val DIN_A4 = Size(595, 842)
        private val PAGE_PADDING = PointF(60f, 60f)
    }
}