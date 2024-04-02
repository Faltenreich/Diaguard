package com.faltenreich.diaguard.tag.delete

import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.EntrySearchScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.list.DeleteTagUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class TagDeleteViewModel(
    private val tag: Tag,
    countEntriesForTag: CountEntriesByTagUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val deleteTag: DeleteTagUseCase = inject(),
) : ViewModel<TagDeleteState, TagDeleteIntent>() {

    override val state: Flow<TagDeleteState> = combine(
        flowOf(tag),
        countEntriesForTag(tag),
        ::TagDeleteState,
    )

    override fun handleIntent(intent: TagDeleteIntent) {
        when (intent) {
            is TagDeleteIntent.Close -> closeModal()
            is TagDeleteIntent.Preview -> navigateToScreen(EntrySearchScreen(query = tag.name))
            is TagDeleteIntent.Confirm -> {
                deleteTag(tag)
                closeModal()
            }
        }
    }
}