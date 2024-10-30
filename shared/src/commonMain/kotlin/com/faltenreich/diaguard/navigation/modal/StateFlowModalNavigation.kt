package com.faltenreich.diaguard.navigation.modal

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StateFlowModalNavigation : ModalNavigation {

    private val _modal = MutableStateFlow<Modal?>(null)
    override val modal = _modal.asStateFlow()

    override fun openModal(modal: Modal) {
        _modal.tryEmit(modal)
    }

    override fun closeModal() {
        _modal.tryEmit(null)
    }
}