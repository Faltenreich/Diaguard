package com.faltenreich.diaguard.preference.list.item.about

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.shared.config.BuildConfig
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.primitive.format
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetAppVersionPreferenceUseCase(
    private val buildConfig: BuildConfig = inject(),
) {

    operator fun invoke(): Flow<Preference> {
        val preference = Preference.Plain(
            title = MR.strings.version,
            subtitle = "%s-%d".format(
                buildConfig.getVersionName(),
                buildConfig.getBuildNumber(),
            ),
            onClick = {},
        )
        return flowOf(preference)
    }
}