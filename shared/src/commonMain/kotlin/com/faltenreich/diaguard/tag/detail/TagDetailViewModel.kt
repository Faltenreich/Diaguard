package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.TagDeleteModal
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class TagDetailViewModel(
    tag: Tag,
    getEntriesOfTag: GetEntriesOfTagUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
) : ViewModel<TagDetailState, TagDetailIntent>() {

    override val state: Flow<TagDetailState> = combine(
        flowOf(tag),
        getEntriesOfTag(tag),
        ::TagDetailState,
    )

    override fun onIntent(intent: TagDetailIntent) {
        when (intent) {
            is TagDetailIntent.EditTag -> Unit
            is TagDetailIntent.DeleteTag -> openModal(TagDeleteModal(intent.tag))
            is TagDetailIntent.OpenEntry -> navigateToScreen(EntryFormScreen(intent.entry))
        }
    }
}