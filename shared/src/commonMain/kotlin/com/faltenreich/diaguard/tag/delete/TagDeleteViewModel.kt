package com.faltenreich.diaguard.tag.delete

import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.list.DeleteTagUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TagDeleteViewModel(
    // FIXME: Stays the same when reopening dialog due to single-instance instead of factory
    private val tag: Tag,
    countEntriesForTag: CountEntriesByTagUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
    private val deleteTag: DeleteTagUseCase = inject(),
) : ViewModel<TagDeleteState, TagDeleteIntent>() {

    override val state: Flow<TagDeleteState> = countEntriesForTag(tag).map(::TagDeleteState)

    override fun onIntent(intent: TagDeleteIntent) {
        when (intent) {
            is TagDeleteIntent.Close -> closeModal()
            is TagDeleteIntent.Confirm -> {
                deleteTag(tag)
                closeModal()
            }
        }
    }
}