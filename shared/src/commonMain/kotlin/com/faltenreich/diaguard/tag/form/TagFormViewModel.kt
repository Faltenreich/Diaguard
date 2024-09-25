package com.faltenreich.diaguard.tag.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.tag.StoreTagUseCase
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.ValidateTagUseCase
import kotlinx.coroutines.flow.emptyFlow

class TagFormViewModel(
    private val validateTag: ValidateTagUseCase,
    private val createTag: StoreTagUseCase,
    private val closeModal: CloseModalUseCase,
) : ViewModel<Unit, TagFormIntent, Unit>() {

    override val state = emptyFlow<Unit>()

    var name: String by mutableStateOf("")
    var error: String? by mutableStateOf(null)

    override suspend fun handleIntent(intent: TagFormIntent) {
        when (intent) {
            is TagFormIntent.Close -> closeModal()
            is TagFormIntent.Submit -> createTagIfValid()
        }
    }

    private fun createTagIfValid() {
        val tag = Tag.User(name)
        when (val result = validateTag(tag)) {
            is ValidationResult.Success -> {
                createTag(tag)
                closeModal()
                error = null
            }
            is ValidationResult.Failure -> {
                error = result.error
            }
        }
    }
}