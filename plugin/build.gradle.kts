plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.android.plugin)
    implementation(libs.compose.plugin)
    implementation(libs.detekt.plugin)
    implementation(libs.kotlin.plugin)
    // Workaround for supporting type-safe version catalog accessors in precompiled script plugin
    // https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files((libs as Any).javaClass.superclass.protectionDomain.codeSource.location))
}