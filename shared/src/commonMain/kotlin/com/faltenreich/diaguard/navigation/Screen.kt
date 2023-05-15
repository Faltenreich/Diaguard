package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.bottom.BottomAppBarOwner
import com.faltenreich.diaguard.navigation.top.TopAppBarOwner

typealias VoyagerScreen = cafe.adriel.voyager.core.screen.Screen

interface Screen : VoyagerScreen, TopAppBarOwner, BottomAppBarOwner