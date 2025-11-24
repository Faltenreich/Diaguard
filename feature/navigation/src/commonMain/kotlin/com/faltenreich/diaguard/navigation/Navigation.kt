package com.faltenreich.diaguard.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Navigation {

    private val _events = MutableSharedFlow<com.faltenreich.diaguard.navigation.NavigationEvent>()
    val events = _events.asSharedFlow()

    private val _topAppBarStyle = MutableStateFlow<com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle>(
        _root_ide_package_.com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.Hidden)
    val topAppBarStyle = _topAppBarStyle.asStateFlow()

    private val _bottomAppBarStyle = MutableStateFlow<com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle>(
        _root_ide_package_.com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle.Visible())
    val bottomAppBarStyle = _bottomAppBarStyle.asStateFlow()

    suspend fun postEvent(event: com.faltenreich.diaguard.navigation.NavigationEvent) {
        _events.emit(event)
    }

    fun setTopAppBarStyle(topAppBarStyle: com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle) {
        _topAppBarStyle.update { topAppBarStyle }
    }

    fun setBottomAppBarStyle(bottomAppBarStyle: com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle) {
        _bottomAppBarStyle.update { bottomAppBarStyle }
    }
}