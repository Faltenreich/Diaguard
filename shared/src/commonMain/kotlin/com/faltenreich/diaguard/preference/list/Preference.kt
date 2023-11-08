package com.faltenreich.diaguard.preference.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DropdownTextMenu
import com.faltenreich.diaguard.shared.view.DropdownTextMenuItem
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.launch

// TODO: Implement Serializable
sealed class Preference(
    val title: StringResource,
    val subtitle: String?,
) {

    @Composable
    abstract fun Content(modifier: Modifier)

    class Folder(
        title: StringResource,
        private val preferences: kotlin.collections.List<Preference>,
    ) : Preference(title, null) {

        @Composable
        override fun Content(modifier: Modifier) {
            val navigator = LocalNavigator.currentOrThrow
            PreferenceListItemLayout(
                preference = this,
                modifier = modifier.clickable {
                    navigator.push(Screen.PreferenceList(preferences))
                },
            )
        }
    }

    class Category(
        title: StringResource,
        private val icon: ImageResource,
    ) : Preference(title, null) {

        @Composable
        override fun Content(modifier: Modifier) {
            Divider()
            Row(
                modifier = modifier
                    .padding(
                        start = AppTheme.dimensions.padding.P_3,
                        top = AppTheme.dimensions.padding.P_3,
                        end = AppTheme.dimensions.padding.P_3,
                        bottom = AppTheme.dimensions.padding.P_2,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.width(AppTheme.dimensions.size.ListOffsetWidth)) {
                    Image(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier.size(AppTheme.dimensions.size.ImageSmall),
                        colorFilter = ColorFilter.tint(AppTheme.colors.scheme.primary),
                    )
                }
                Text(
                    text = getString(title),
                    color = AppTheme.colors.scheme.primary,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }

    class Action(
        title: StringResource,
        subtitle: String?,
        // FIXME: Find way to pass Composable context
        private val action: (Navigator) -> Unit,
    ) : Preference(title, subtitle) {

        @Composable
        override fun Content(modifier: Modifier) {
            val navigator = LocalNavigator.currentOrThrow
            PreferenceListItemLayout(
                preference = this,
                modifier = modifier.clickable { action(navigator) },
            )
        }
    }

    class List(
        title: StringResource,
        subtitle: String?,
        private val options: kotlin.collections.List<Option>,
    ) : Preference(title, subtitle) {

        @Composable
        override fun Content(modifier: Modifier) {
            val scope = rememberCoroutineScope()
            var isExpanded by rememberSaveable { mutableStateOf(false) }
            Box(modifier = modifier) {
                PreferenceListItemLayout(
                    preference = this@List,
                    modifier = Modifier.clickable { isExpanded = true },
                )
                DropdownTextMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    items = options.map { option ->
                        DropdownTextMenuItem(
                            label = option.label(),
                            onClick = { scope.launch { option.onSelected() } },
                            isSelected = { option.isSelected },
                        )
                    }
                )
            }
        }

        data class Option(
            val label: @Composable () -> String,
            val isSelected: Boolean,
            val onSelected: suspend () -> Unit,
        )
    }
}