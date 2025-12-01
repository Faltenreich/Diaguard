package com.faltenreich.diaguard.tag.detail

import androidx.paging.Pager
import androidx.paging.cachedIn
import com.faltenreich.diaguard.architecture.either.ValidationResult
import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.data.navigation.NavigationTarget
import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.entry.form.StoreEntryUseCase
import com.faltenreich.diaguard.entry.list.EntryListPagingSource
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToUseCase
import com.faltenreich.diaguard.tag.StoreTagUseCase
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
    private val deleteEntry: DeleteEntryUseCase = inject(),
    private val storeEntry: StoreEntryUseCase = inject(),
    private val navigateTo: NavigateToUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
) : ViewModel<TagDetailState, TagDetailIntent, Unit>() {

    private val tag: Tag.Local = checkNotNull(getTagById(tagId))

    private val name = MutableStateFlow(tag.name)
    private val error = MutableStateFlow<String?>(null)
    private val deleteDialog = MutableStateFlow<TagDetailState.DeleteDialog?>(null)
    private lateinit var pagingSource: EntryListPagingSource

    override val state = combine(
        name,
        error,
        deleteDialog,
        ::TagDetailState,
    )


    val pagingData = Pager(
        config = EntryListPagingSource.newConfig(),
        pagingSourceFactory = {
            EntryListPagingSource(
                getData = { page -> getEntriesOfTag(tag, page) },
            ).also { pagingSource = it }
        },
    ).flow.cachedIn(scope)

    override suspend fun handleIntent(intent: TagDetailIntent) {
        when (intent) {
            is TagDetailIntent.SetName -> name.update { intent.name }
            is TagDetailIntent.UpdateTag -> updateTag()
            is TagDetailIntent.OpenDeleteDialog -> deleteDialog.update { TagDetailState.DeleteDialog }
            is TagDetailIntent.CloseDeleteDialog -> deleteDialog.update { null }
            is TagDetailIntent.DeleteTag -> deleteTag()
            is TagDetailIntent.OpenEntry -> navigateTo(NavigationTarget.EntryForm(entryId = intent.entry.id))
            is TagDetailIntent.DeleteEntry -> deleteEntry(intent.entry)
            is TagDetailIntent.RestoreEntry -> {
                storeEntry(intent.entry)
                pagingSource.invalidate()
            }
            is TagDetailIntent.OpenEntrySearch -> navigateTo(NavigationTarget.EntrySearch(intent.query))
        }
    }

    private suspend fun updateTag() {
        val tag = tag.copy(name = name.value)
        when (val result = validateTag(tag)) {
            is ValidationResult.Success -> {
                storeTag(tag)
                navigateBack()
            }
            is ValidationResult.Failure -> {
                error.update { result.error }
            }
        }
    }

    private suspend fun deleteTag() {
        deleteTag(tag)
        navigateBack()
    }
}