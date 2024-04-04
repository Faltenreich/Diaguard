package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.datetime.factory.DateTimeConstants
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.DeleteModal
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.UpdateTagUseCase
import com.faltenreich.diaguard.tag.form.ValidateTagUseCase
import com.faltenreich.diaguard.tag.form.DeleteTagUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class TagDetailViewModel(
    private val tag: Tag,
    getEntriesOfTag: GetEntriesOfTagUseCase = inject(),
    private val validateTag: ValidateTagUseCase = inject(),
    private val updateTag: UpdateTagUseCase = inject(),
    private val deleteTag: DeleteTagUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
) : ViewModel<TagDetailState, TagDetailIntent>() {

    var name = MutableStateFlow(tag.name)
    private val error = MutableStateFlow<String?>(null)

    override val state: Flow<TagDetailState> = combine(
        flowOf(tag),
        getEntriesOfTag(tag),
        error,
        ::TagDetailState,
    )

    init {
        scope.launch {
            name.debounce(DateTimeConstants.INPUT_DEBOUNCE)
                .collectLatest { name -> updateTag(tag.copy(name = name)) }
        }
    }

    override fun handleIntent(intent: TagDetailIntent) {
        when (intent) {
            is TagDetailIntent.EditTag -> updateTag(intent.tag, intent.name)
            is TagDetailIntent.DeleteTag -> deleteTag()
            is TagDetailIntent.OpenEntry -> navigateToScreen(EntryFormScreen(intent.entry))
        }
    }

    private fun updateTag(tag: Tag, name: String) {
        when (val result = validateTag(name)) {
            is ValidationResult.Success -> {
                // TODO: Persist tag with new name
                tag.copy(name = name)
                error.value = null
            }
            is ValidationResult.Failure -> {
                error.value = result.error
            }
        }
    }

    private fun deleteTag() {
        openModal(
            DeleteModal(
                onDismissRequest = closeModal::invoke,
                onConfirm = {
                    deleteTag(tag)
                    closeModal()
                    navigateBack()
                },
            )
        )
    }
}