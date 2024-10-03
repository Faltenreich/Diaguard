package com.faltenreich.diaguard.tag.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.list.EntryListPagingSource
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateToScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.view.DeleteModal
import com.faltenreich.diaguard.tag.StoreTagUseCase
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.ValidateTagUseCase
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class TagDetailViewModel(
    tagId: Long,
    getTagById: GetTagByIdUseCase = inject(),
    getEntriesOfTag: GetEntriesOfTagUseCase = inject(),
    private val validateTag: ValidateTagUseCase = inject(),
    private val storeTag: StoreTagUseCase = inject(),
    private val deleteTag: DeleteTagUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
) : ViewModel<Unit, TagDetailIntent, Unit>() {

    override val state = emptyFlow<Unit>()

    private val tag: Tag.Local = requireNotNull(getTagById(tagId))

    var name: String by mutableStateOf(tag.name)
    var error: String? by mutableStateOf(null)

    val pagingData = Pager(
        config = EntryListPagingSource.newConfig(),
        pagingSourceFactory = {
            EntryListPagingSource(
                getEntries = { page ->
                    getEntriesOfTag(tag)
                },
            )
        },
    ).flow.cachedIn(scope)

    override suspend fun handleIntent(intent: TagDetailIntent) = with(intent) {
        when (this) {
            is TagDetailIntent.UpdateTag -> updateTag()
            is TagDetailIntent.DeleteTag -> deleteTag()
            is TagDetailIntent.OpenEntry -> navigateToScreen(EntryFormScreen(entry))
            is TagDetailIntent.OpenEntrySearch -> navigateToScreen(EntrySearchScreen(query))
        }
    }

    private suspend fun updateTag() {
        val tag = tag.copy(name = name)
        when (val result = validateTag(tag)) {
            is ValidationResult.Success -> {
                storeTag(tag)
                navigateBack()
            }
            is ValidationResult.Failure -> {
                error = result.error
            }
        }
    }

    private fun deleteTag() {
        openModal(
            DeleteModal(
                onDismissRequest = closeModal::invoke,
                onConfirmRequest = {
                    deleteTag(tag)
                    closeModal()
                    scope.launch { navigateBack() }
                },
            )
        )
    }
}