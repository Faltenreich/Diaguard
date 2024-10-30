package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.snack.SnackbarNavigation
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.bottomsheet.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.modal.ModalNavigation
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.screen.ScreenNavigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

    private val _currentScreen = MutableStateFlow<Screen?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    private val _topAppBarStyle = MutableStateFlow<TopAppBarStyle>(TopAppBarStyle.Hidden)
    val topAppBarStyle = _topAppBarStyle.asStateFlow()

    private val _bottomAppBarStyle = MutableStateFlow<BottomAppBarStyle>(BottomAppBarStyle.Visible())
    val bottomAppBarStyle = _bottomAppBarStyle.asStateFlow()

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.update { screen }
    }

    fun setTopAppBarStyle(topAppBarStyle: TopAppBarStyle) {
        _topAppBarStyle.update { topAppBarStyle }
    }

    fun setBottomAppBarStyle(bottomAppBarStyle: BottomAppBarStyle) {
        _bottomAppBarStyle.update { bottomAppBarStyle }
    }
}