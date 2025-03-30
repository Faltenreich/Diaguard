package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.backup.legacy.ImportLegacyUseCase
import com.faltenreich.diaguard.backup.seed.ImportSeedUseCase
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.preference.version.VersionCodePreference
import com.faltenreich.diaguard.shared.config.BuildConfig
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
            importSeed()
            importLegacy()
            setPreference(VersionCodePreference, buildConfig.getVersionCode())
        }
    }
}