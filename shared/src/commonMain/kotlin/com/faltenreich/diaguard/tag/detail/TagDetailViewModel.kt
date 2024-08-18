package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.view.DeleteModal
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.form.DeleteTagUseCase
import com.faltenreich.diaguard.tag.form.StoreTagUseCase
import com.faltenreich.diaguard.tag.form.ValidateTagUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
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
) : ViewModel<TagDetailState, TagDetailIntent, Unit>() {

    private val tag: Tag.Local = requireNotNull(getTagById(tagId))
    var name = MutableStateFlow(tag.name)
    private val error = MutableStateFlow<String?>(null)

    override val state: Flow<TagDetailState> = combine(
        getEntriesOfTag(tag),
        error,
        ::TagDetailState,
    )

    override suspend fun handleIntent(intent: TagDetailIntent) = with(intent) {
        when (this) {
            is TagDetailIntent.UpdateTag -> updateTag()
            is TagDetailIntent.DeleteTag -> deleteTag()
            is TagDetailIntent.OpenEntry -> navigateToScreen(EntryFormScreen(entry))
            is TagDetailIntent.OpenEntrySearch -> navigateToScreen(EntrySearchScreen(query))
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
                // FIXME: Not shown anymore
                error.value = result.error
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