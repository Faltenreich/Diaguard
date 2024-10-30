package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.bar.snack.SnackbarNavigation
import com.faltenreich.diaguard.navigation.bottomsheet.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.modal.ModalNavigation
import com.faltenreich.diaguard.navigation.screen.ScreenNavigation

class Navigation(
    private val screenNavigation: ScreenNavigation,
    private val bottomSheetNavigation: BottomSheetNavigation,
    private val modalNavigation: ModalNavigation,
    private val snackbarNavigation: SnackbarNavigation,
) : ScreenNavigation by screenNavigation,
    BottomSheetNavigation by bottomSheetNavigation,
    ModalNavigation by modalNavigation,
    SnackbarNavigation by snackbarNavigation