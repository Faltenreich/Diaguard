package com.faltenreich.diaguard

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.faltenreich.diaguard.system.notification.Shortcut
import com.faltenreich.diaguard.system.notification.forAction
import com.faltenreich.diaguard.system.permission.AndroidPermissionManager
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
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )

        val shortcut = intent?.action?.let(Shortcut::forAction)

        setContent {
            AppView(shortcut)
        }
    }
}