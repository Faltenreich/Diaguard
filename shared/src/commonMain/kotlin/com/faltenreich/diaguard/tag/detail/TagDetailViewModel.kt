package com.faltenreich.diaguard.tag.detail

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
import com.faltenreich.diaguard.tag.form.DeleteTagUseCase
import com.faltenreich.diaguard.tag.form.StoreTagUseCase
import com.faltenreich.diaguard.tag.form.ValidateTagUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class TagDetailViewModel(
    private val tag: Tag.Local,
    getEntriesOfTag: GetEntriesOfTagUseCase = inject(),
    private val validateTag: ValidateTagUseCase = inject(),
    private val storeTag: StoreTagUseCase = inject(),
    private val deleteTag: DeleteTagUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
) : ViewModel<TagDetailState, TagDetailIntent, Unit>() {

    var name = MutableStateFlow(tag.name)
    private val error = MutableStateFlow<String?>(null)

    override val state: Flow<TagDetailState> = combine(
        getEntriesOfTag(tag),
        error,
        ::TagDetailState,
    )

    override fun handleIntent(intent: TagDetailIntent) {
        when (intent) {
            is TagDetailIntent.UpdateTag -> updateTag()
            is TagDetailIntent.DeleteTag -> deleteTag()
            is TagDetailIntent.OpenEntry -> navigateToScreen(EntryFormScreen(intent.entry))
        }
    }

    private fun updateTag() {
        val tag = tag.copy(name = name.value)
        when (val result = validateTag(tag)) {
            is ValidationResult.Success -> {
                storeTag(tag)
                navigateBack()
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
                onConfirmRequest = {
                    deleteTag(tag)
                    closeModal()
                    navigateBack()
                },
            )
        )
    }
}