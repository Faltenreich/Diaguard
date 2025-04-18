package com.faltenreich.diaguard.preference.license

import com.faltenreich.diaguard.shared.file.FileReader
import com.mikepenz.aboutlibraries.Libs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetLicensesUseCase(private val fileReader: FileReader) {

    operator fun invoke(): Flow<Libs> {
        val json = fileReader.read()
        val libraries = Libs.Builder()
            .withJson(json)
            .build()
        return flowOf(libraries)
    }
}