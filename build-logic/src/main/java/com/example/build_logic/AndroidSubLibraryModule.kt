package com.example.build_logic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidSubLibraryModule : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.android")


            extensions.getByType<LibraryExtension>().run {
                compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                    minSdk = libs.findVersion("minSdk").get().toString().toInt()
                }
                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }
            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_11.toString()
                }
            }
            dependencies {
                "implementation"(libs.findLibrary("android.material").get())
            }
        }
    }
}