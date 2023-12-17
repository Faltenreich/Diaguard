package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class PreferenceListViewModel(
    preferences: List<PreferenceListItem>?,
    getDefaultPreferences: GetDefaultPreferencesUseCase = inject(),
    dispatcher: CoroutineDispatcher = inject(),
) : ViewModel<PreferenceListViewState, Unit>() {

    override val state = (preferences?.let(::flowOf) ?: getDefaultPreferences())
        .map(PreferenceListViewState::Loaded)
        .flowOn(dispatcher)

    override fun onIntent(intent: Unit) = Unit
}