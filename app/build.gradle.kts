plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.vkapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.vkapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        addManifestPlaceholders(
            mapOf(
                "VKIDRedirectHost" to "vk.com",
                "VKIDRedirectScheme" to "vk51984651",
                "VKIDClientID" to "51984651",
                "VKIDClientSecret" to "zF0W7jwhEZqzOlvynKEC"
            )
        )
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
        // Enable core library desugaring
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.exoplayer.hls)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.gson)
    implementation(libs.android.sdk.core)
    implementation(libs.android.sdk.api)
    implementation(libs.vkid)
    implementation(libs.onetap.compose)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.coil.compose)

    implementation(libs.androidx.media3.exoplayer)

    implementation(libs.androidx.media3.ui)

    implementation(libs.androidx.webkit)

    implementation(libs.accompanist.webview)

    implementation("com.google.dagger:dagger:2.51")
    kapt("com.google.dagger:dagger-compiler:2.51")


}


