plugins {
    alias(libs.plugins.android.app.weather.lib)
}

android {
    namespace = "com.plugin.conventions.data"
}

dependencies {
    implementation(libs.bundles.retrofit)
}