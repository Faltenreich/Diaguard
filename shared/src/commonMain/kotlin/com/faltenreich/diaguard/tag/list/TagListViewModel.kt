package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.TagFormModal
import com.faltenreich.diaguard.navigation.screen.TagDetailScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.map

class TagListViewModel(
    getTags: GetTagsUseCase,
    private val openModal: OpenModalUseCase,
    private val navigateToScreen: NavigateToScreenUseCase,
) : ViewModel<TagListViewState, TagListIntent>() {

    private val tags = getTags()
    override val state = tags.map(::TagListViewState)

    override fun handleIntent(intent: TagListIntent) {
        when (intent) {
            is TagListIntent.CreateTag -> openModal(TagFormModal)
            is TagListIntent.OpenTag -> navigateToScreen(TagDetailScreen(intent.tag))
        }
    }
}