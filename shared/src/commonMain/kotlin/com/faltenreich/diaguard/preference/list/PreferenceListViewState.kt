package com.faltenreich.diaguard.preference.list

sealed interface PreferenceListViewState {

    data object Loading : PreferenceListViewState

    data class Loaded(val listItems: List<Preference>) : PreferenceListViewState
}