package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.PreferenceStore
import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.preference.list.item.SelectablePreferenceOption
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.config.BuildConfig
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.primitive.format
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PreferenceListViewModel(
    preferenceStore: PreferenceStore = inject(),
    buildConfig: BuildConfig = inject(),
) : ViewModel() {

    private val startScreen = preferenceStore.startScreen
    private val version = flowOf("%s-%d".format(buildConfig.getVersionName(), buildConfig.getBuildNumber()))

    private val listItems: Flow<List<Preference>> = combine(
        startScreen,
        version,
    ) { startScreen, version ->
        listOf(
            Preference.Plain(
                title = { stringResource(MR.strings.version) },
                subtitle = { version },
            ),
            Preference.Selection(
                title = { stringResource(MR.strings.start_screen) },
                subtitle = { stringResource(startScreen.labelResource) },
                options = StartScreen.values().map { value ->
                    SelectablePreferenceOption(
                        label = { stringResource(value.labelResource) },
                        isSelected = value == startScreen,
                        onSelected = { preferenceStore.setStartScreen(value) },
                    )
                },
            ),
        )
    }

    private val state = listItems.map(::PreferenceListViewState)
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = PreferenceListViewState(
            listItems = emptyList(),
        ),
    )
}