package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.preference.list.item.PreferenceListItem

sealed interface PreferenceListViewState {

    data object Loading : PreferenceListViewState

    data class Loaded(val listItems: List<PreferenceListItem>) : PreferenceListViewState
}