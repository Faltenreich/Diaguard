package com.faltenreich.diaguard.navigation.modal

import kotlinx.coroutines.flow.StateFlow

interface ModalNavigation {

    val modal: StateFlow<Modal?>

    fun openModal(modal: Modal)

    fun closeModal()
}