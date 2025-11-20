plugins {
    id("multiplatform-convention")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:logging"))
                implementation(libs.sqldelight.coroutines)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.sqldelight.android)
                implementation(libs.sqldelight.jvm)
            }
        }
    }
}

sqldelight {
    databases {
        create("SqlDelightApi") {
            val appNamespace: String by rootProject.extra
            packageName.set("$appNamespace.database.sqldelight")
        }
    }
}