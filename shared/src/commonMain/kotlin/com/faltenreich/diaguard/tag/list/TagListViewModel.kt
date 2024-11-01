package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.tag.detail.TagDetailScreen
import com.faltenreich.diaguard.tag.form.TagFormModal
import kotlinx.coroutines.flow.map

class TagListViewModel(
    getTags: GetTagsUseCase,
    private val pushScreen: PushScreenUseCase,
    private val openModal: OpenModalUseCase,
) : ViewModel<TagListState, TagListIntent, Unit>() {

    private val tags = getTags()
    override val state = tags.map(::TagListState)

    override suspend fun handleIntent(intent: TagListIntent) {
        when (intent) {
            is TagListIntent.CreateTag -> openModal(TagFormModal)
            is TagListIntent.OpenTag -> pushScreen(TagDetailScreen(intent.tag))
        }
    }
}