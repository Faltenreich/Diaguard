package com.faltenreich.diaguard

import android.app.Activity
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import com.faltenreich.diaguard.architecture.either.ValidationRule
import com.faltenreich.diaguard.entry.form.measurement.ValidateEntryFormInputUseCase
import com.faltenreich.diaguard.food.search.FoodSearchMode
import com.faltenreich.diaguard.food.search.FoodSearchViewModel
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListMode
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListViewModel
import com.faltenreich.diaguard.measurement.unit.usecase.ValidateMeasurementUnitUseCase
import com.faltenreich.diaguard.tag.ValidateTagUseCase
import io.ktor.client.engine.HttpClientEngine
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify
import java.io.File

class AppModuleTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun dependencyGraphIsValid() {
        appModule().verify(
            extraTypes = listOf(
                Activity::class,
                Context::class,
                File::class,
                HttpClientEngine::class,
                SqlDriver::class,
            ),
            injections = injectedParameters(
                definition<FoodSearchViewModel>(FoodSearchMode::class),
                definition<MeasurementUnitListViewModel>(MeasurementUnitListMode::class),
                definition<ValidateEntryFormInputUseCase>(List::class, ValidationRule::class),
                definition<ValidateMeasurementUnitUseCase>(List::class),
                definition<ValidateTagUseCase>(List::class),
            ),
        )
    }
}