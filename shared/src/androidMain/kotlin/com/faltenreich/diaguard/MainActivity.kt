package com.faltenreich.diaguard

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.faltenreich.diaguard.shared.notification.Shortcut
import com.faltenreich.diaguard.shared.notification.forAction
import com.faltenreich.diaguard.shared.permission.AndroidPermissionManager
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class MainActivity : ComponentActivity() {

    private val permissionManager: AndroidPermissionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadKoinModules(
            listOf(
                module { single<Activity> { this@MainActivity } },
                module { single<ComponentActivity> { this@MainActivity } },
            )
        )

        // LifecycleOwners must call register before they are STARTED
        permissionManager.bind(this)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.Companion.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.Companion.dark(Color.TRANSPARENT),
        )

        setContent {
            AppView(shortcut = intent?.action?.let(Shortcut::forAction))
        }
    }
}