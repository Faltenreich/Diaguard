package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.validation.ValidationResult
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
    private val form = MutableStateFlow<TagListState.Form>(TagListState.Form.Hidden)
    override val state = combine(
        tags,
        form,
        ::TagListState,
    )

    override suspend fun handleIntent(intent: TagListIntent) = with(intent) {
        when (this) {
            is TagListIntent.OpenFormDialog -> form.update { TagListState.Form.Shown() }
            is TagListIntent.CloseFormDialog -> form.update { TagListState.Form.Hidden }
            is TagListIntent.OpenTag -> pushScreen(TagDetailScreen(tag))
            is TagListIntent.StoreTag -> createTagIfValid(this)
        }
    }

    private fun createTagIfValid(intent: TagListIntent.StoreTag) {
        val tag = Tag.User(intent.name)
        when (val result = validateTag(tag)) {
            is ValidationResult.Success -> {
                form.update { TagListState.Form.Hidden }
                createTag(tag)
            }
            is ValidationResult.Failure -> form.update { TagListState.Form.Shown(error = result.error) }
        }
    }
}