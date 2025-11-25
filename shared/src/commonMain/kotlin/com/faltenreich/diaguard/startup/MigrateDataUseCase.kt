package com.faltenreich.diaguard.startup

import com.faltenreich.diaguard.preference.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.SetPreferenceUseCase
import com.faltenreich.diaguard.data.preference.version.VersionCodePreference
import com.faltenreich.diaguard.config.BuildConfig
import com.faltenreich.diaguard.logging.Logger
import com.faltenreich.diaguard.startup.legacy.ImportLegacyUseCase
import com.faltenreich.diaguard.startup.seed.ImportSeedUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class MigrateDataUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val getPreference: GetPreferenceUseCase,
    private val setPreference: SetPreferenceUseCase,
    private val buildConfig: BuildConfig,
    private val importSeed: ImportSeedUseCase,
    private val importLegacy: ImportLegacyUseCase,
) {

    suspend operator fun invoke() = withContext(dispatcher) {
        val versionCode = getPreference(VersionCodePreference).first()
        if (versionCode <= 0) {
            Logger.debug("Migration started")
            importSeed()
            importLegacy()
            setPreference(VersionCodePreference, buildConfig.getVersionCode())
            Logger.debug("Migration completed")
        }
    }
}