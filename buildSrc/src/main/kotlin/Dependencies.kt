import org.gradle.api.JavaVersion

object Configuration {

    object Project {

        const val nameSpace = "com.faltenreich.diaguard"
        // TODO: Increment version code
        const val versionCode = 60
        const val versionName = "4.0.0"
    }

    object Java {

        val version = JavaVersion.VERSION_17
    }

    object Android {

        const val compileSdk = 34
        const val minSdk = 21
        const val targetSdk = 34
    }
}