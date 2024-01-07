package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.TagDeleteModal
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.validation.ValidateUseCase
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.UpdateTagUseCase
import com.faltenreich.diaguard.tag.form.RedundantTagException
import com.faltenreich.diaguard.tag.form.UniqueTagRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class TagDetailViewModel(
    tag: Tag,
    getEntriesOfTag: GetEntriesOfTagUseCase = inject(),
    private val updateTag: UpdateTagUseCase = inject(),
    private val validate: ValidateUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
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

    override fun onIntent(intent: TagDetailIntent) {
        when (intent) {
            is TagDetailIntent.EditTag -> editTagIfValid(intent.tag, intent.name)
            is TagDetailIntent.DeleteTag -> openModal(TagDeleteModal(intent.tag))
            is TagDetailIntent.OpenEntry -> navigateToScreen(EntryFormScreen(intent.entry))
        }
    }

    private fun editTagIfValid(tag: Tag, name: String) {
        val result = validate(name, UniqueTagRule())
        if (result.isSuccess) {
            // TODO
            error.value = null
        } else {
            error.value = when (result.exceptionOrNull()) {
                is RedundantTagException -> getString(MR.strings.tag_already_taken)
                else -> getString(MR.strings.error_unknown)
            }
        }
    }
}