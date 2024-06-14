package com.example.build_logic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidApplication : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.android")
            extensions.configure<ApplicationExtension> {
                compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
                defaultConfig {
                    minSdk = libs.findVersion("minSdk").get().toString().toInt()
                    targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
                    versionCode = libs.findVersion("versionCode").get().toString().toInt()
                    applicationId = libs.findVersion("applicationId").get().toString()
                    versionName = libs.findVersion("versionName").get().toString()
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    vectorDrawables {
                        useSupportLibrary = true
                    }
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
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion = libs
                        .findVersion("composeCompiler")
                        .get()
                        .toString()
                }
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
                tasks.withType<KotlinCompile>().configureEach {
                    kotlinOptions {
                        jvmTarget = JavaVersion.VERSION_11.toString()
                    }
                }
            }
        }
    }
}