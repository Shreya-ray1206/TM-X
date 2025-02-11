import org.gradle.kotlin.dsl.support.kotlinCompilerOptions
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation("androidx.core:core-ktx:1.15.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
            implementation(libs.ktor.client.okhttp)
            implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0") // Android-only
            implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0") // Android-only
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Removed Android-specific lifecycle dependencies for commonMain
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
            implementation(libs.coil.network.ktor3)
            implementation(libs.ktor.client.core)

            implementation(libs.lifecycle.viewmodel)
            implementation(libs.lifecycle.viewmodel.compose)
            api(libs.moko.permissions)
            api(libs.moko.permissions.compose)

            implementation("androidx.core:core-ktx:1.15.0")
            api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "org.kibbcom.tm_x"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.kibbcom.tm_x"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.bluetooth)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.kibbcom.tm_x.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.kibbcom.tm-x"
            packageVersion = "1.0.0"
        }
    }
}
