package com.faltenreich.diaguard.tag.detail

import androidx.paging.Pager
import androidx.paging.cachedIn
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.list.EntryListPagingSource
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.tag.StoreTagUseCase
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.ValidateTagUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class TagDetailViewModel(
    tagId: Long,
    getTagById: GetTagByIdUseCase = inject(),
    getEntriesOfTag: GetEntriesOfTagUseCase = inject(),
    private val validateTag: ValidateTagUseCase = inject(),
    private val storeTag: StoreTagUseCase = inject(),
    private val deleteTag: DeleteTagUseCase = inject(),
    private val pushScreen: PushScreenUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
) : ViewModel<TagDetailState, TagDetailIntent, Unit>() {

    private val tag: Tag.Local = checkNotNull(getTagById(tagId))

    private val name = MutableStateFlow(tag.name)
    private val error = MutableStateFlow<String?>(null)
    private val deleteDialog = MutableStateFlow<TagDetailState.DeleteDialog?>(null)

    override val state = combine(
        name,
        error,
        deleteDialog,
        ::TagDetailState,
    )


    val pagingData = Pager(
        config = EntryListPagingSource.newConfig(),
        pagingSourceFactory = {
            EntryListPagingSource(getData = { page -> getEntriesOfTag(tag, page) })
        },
    ).flow.cachedIn(scope)

    override suspend fun handleIntent(intent: TagDetailIntent) {
        when (intent) {
            is TagDetailIntent.SetName -> name.update { intent.name }
            is TagDetailIntent.UpdateTag -> updateTag()
            is TagDetailIntent.OpenDeleteDialog -> deleteDialog.update { TagDetailState.DeleteDialog }
            is TagDetailIntent.CloseDeleteDialog -> deleteDialog.update { null }
            is TagDetailIntent.DeleteTag -> deleteTag()
            is TagDetailIntent.OpenEntry -> pushScreen(EntryFormScreen(intent.entry))
            is TagDetailIntent.DeleteEntry -> TODO()
            is TagDetailIntent.RestoreEntry -> TODO()
            is TagDetailIntent.OpenEntrySearch -> pushScreen(EntrySearchScreen(intent.query))
        }
    }

    private suspend fun updateTag() {
        val tag = tag.copy(name = name.value)
        when (val result = validateTag(tag)) {
            is ValidationResult.Success -> {
                storeTag(tag)
                popScreen()
            }
            is ValidationResult.Failure -> {
                error.update { result.error }
            }
        }
    }

    private suspend fun deleteTag() {
        deleteTag(tag)
        popScreen()
    }
}