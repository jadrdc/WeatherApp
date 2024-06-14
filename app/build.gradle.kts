plugins {
    alias(libs.plugins.android.app.weather)
}

android {
    namespace = "com.plugin.conventions.weather"
}

dependencies {

    implementation(libs.androidx.core.core.ktx)
    //androidx-lifecycle-runtime-ktx
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.activity.compose)
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.material3:material3")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation(libs.koin.android.android)
    implementation(libs.koin.android.compat)
    implementation(libs.koin.android.workmanager)

    implementation(libs.android.compose.ui)
    implementation(libs.android.compose.ui.graph)
    debugImplementation(libs.android.compose.ui.tooling)
    implementation(libs.android.compose.ui.tooling.preview)
    implementation(libs.android.compose.ui.test)
    debugImplementation(libs.android.compose.ui.test.manifest)
    implementation(libs.accompanist.permissions)
    // Koin dependencies
    implementation(libs.bundles.retrofit)
    implementation(libs.coil)
    implementation(libs.bundles.koin)
    implementation(project(":weather"))
    implementation(project(":data"))
    implementation(project(":domain"))
}