plugins {
    alias(libs.plugins.android.app.weather.lib)
}

android {
    namespace = "com.plugin.conventions.weather"
}

dependencies {
    implementation(libs.bundles.android.core)
    implementation(project(":data"))
    implementation(project(":domain"))

    //ViewModel lifecycle
    implementation(libs.android.lifecycle.livedata)
    implementation(libs.android.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.coil)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.android.compose.ui.test)
}