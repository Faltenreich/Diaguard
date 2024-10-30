package com.faltenreich.diaguard.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import com.faltenreich.diaguard.navigation.bar.snack.AndroidxSnackbarNavigation
import com.faltenreich.diaguard.navigation.bar.snack.SnackbarNavigation
import com.faltenreich.diaguard.navigation.bottomsheet.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.modal.ModalNavigation
import com.faltenreich.diaguard.navigation.screen.AndroidxScreenNavigation
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
{

    // TODO: Decouple
    var navController: NavController
        get() = (screenNavigation as AndroidxScreenNavigation).navController
        set(value) { (screenNavigation as AndroidxScreenNavigation).navController = value }

    var snackbarState: SnackbarHostState
        get() = (snackbarNavigation as AndroidxSnackbarNavigation).snackbarState
        set(value) { (snackbarNavigation as AndroidxSnackbarNavigation).snackbarState = value }
}