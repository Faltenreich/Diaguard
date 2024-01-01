package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.screen.TagFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.map

class TagListViewModel(
    getTags: GetTagsUseCase,
    private val deleteTag: DeleteTagUseCase,
    private val openModal: OpenModalUseCase,
) : ViewModel<TagListViewState, TagListIntent>() {

    private val tags = getTags()
    override val state = tags.map(::TagListViewState)

    override fun onIntent(intent: TagListIntent) {
        when (intent) {
            is TagListIntent.Delete -> deleteTag(intent.tag)
            is TagListIntent.OpenForm -> openModal(TagFormScreen)
        }
    }
}