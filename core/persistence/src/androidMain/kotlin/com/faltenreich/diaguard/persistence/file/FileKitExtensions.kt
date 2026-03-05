package com.faltenreich.diaguard.persistence.file

import android.content.Context
import android.os.Environment
import com.faltenreich.diaguard.injection.inject
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile

actual val FileKit.documentsDir: PlatformFile?
    get() {
        val context = inject<Context>()
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: return null
        directory.mkdirs()
        return PlatformFile(directory.absolutePath)
    }