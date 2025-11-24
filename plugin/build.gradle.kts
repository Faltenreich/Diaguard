plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Workaround for supporting type-safe version catalog accessors in precompiled script plugin
    // https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files((libs as Any).javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.android.plugin)
    implementation(libs.compose.plugin)
    implementation(libs.compose.compiler.plugin)
    implementation(libs.detekt.plugin)
    implementation(libs.kotlin.plugin)
    implementation(libs.kover.plugin)
}