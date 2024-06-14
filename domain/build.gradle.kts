plugins {
    alias(libs.plugins.android.app.weather.lib)
}

android {
    namespace = "com.plugin.conventions.domain"
}

dependencies {
    implementation(libs.bundles.android.core)
    implementation(project(":data"))
    implementation(libs.play.services.location)
    implementation(libs.bundles.retrofit)
}