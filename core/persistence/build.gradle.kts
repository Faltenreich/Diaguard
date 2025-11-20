plugins {
    id("multiplatform-convention")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:injection"))
                implementation(project(":core:localization"))
                implementation(project(":core:logging"))
                implementation(libs.datastore)
                implementation(libs.kotlinx.io)
                implementation(libs.sqldelight.coroutines)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidx.preferences)
                implementation(libs.sqldelight.android)
                implementation(libs.sqldelight.jvm)
            }
        }
        @Suppress("unused")
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(libs.androidx.test.runner)
                implementation(libs.androidx.test.junit)
                implementation(libs.androidx.test.junit.ktx)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.turbine)
            }
        }
    }
}

sqldelight {
    databases {
        create("SqlDelightApi") {
            val appNamespace: String by rootProject.extra
            packageName.set("$appNamespace.persistence.sqldelight")
        }
    }
}