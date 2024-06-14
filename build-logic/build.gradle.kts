@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    `kotlin-dsl`
}
group = "com.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}
gradlePlugin {
    plugins {
        register("androidApp") {
            id = "android.app.weather"
            implementationClass = "com.example.build_logic.AndroidApplication"
        }
        register("androidSubLib") {
            id = "android.app.weather.lib"
            implementationClass = "com.example.build_logic.AndroidSubLibraryModule"
        }
    }
}
