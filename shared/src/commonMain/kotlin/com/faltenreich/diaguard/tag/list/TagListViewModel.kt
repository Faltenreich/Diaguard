package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.architecture.either.ValidationResult
import com.faltenreich.diaguard.tag.StoreTagUseCase
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.ValidateTagUseCase
import com.faltenreich.diaguard.tag.detail.TagDetailScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class TagListViewModel(
    getTags: GetTagsUseCase,
    private val pushScreen: PushScreenUseCase,
    private val validateTag: ValidateTagUseCase,
    private val createTag: StoreTagUseCase,
) : ViewModel<TagListState, TagListIntent, Unit>() {

    private val tags = getTags()
    private val formDialog = MutableStateFlow<TagListState.FormDialog?>(null)
    override val state = combine(
        tags,
        formDialog,
        ::TagListState,
    )

    override suspend fun handleIntent(intent: TagListIntent) = with(intent) {
        when (this) {
            is TagListIntent.OpenFormDialog -> formDialog.update { TagListState.FormDialog() }
            is TagListIntent.CloseFormDialog -> formDialog.update { null }
            is TagListIntent.OpenTag -> pushScreen(TagDetailScreen(tag))
            is TagListIntent.StoreTag -> createTagIfValid(this)
        }
    }

    private fun createTagIfValid(intent: TagListIntent.StoreTag) {
        val tag = Tag.User(intent.name)
        when (val result = validateTag(tag)) {
            is ValidationResult.Success -> {
                formDialog.update { null }
                createTag(tag)
            }
            is ValidationResult.Failure -> {
                formDialog.update { TagListState.FormDialog(error = result.error) }
            }
        }
    }
}