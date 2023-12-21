package com.faltenreich.diaguard.navigation.screen

import cafe.adriel.voyager.core.screen.Screen as VoyagerScreen

/**
 * Component that can be navigated to
 *
 * State restoration requires every parameter to implement
 * [com.faltenreich.diaguard.shared.serialization.Serializable]
 */
sealed interface Screen : VoyagerScreen