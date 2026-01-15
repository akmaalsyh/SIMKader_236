plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Plugin untuk database Room (KSP) dan Retrofit (Serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.simkader_236"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.simkader_236"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.compose.icons) // Untuk ikon navigasi dan CRUD [cite: 687, 705]

    // --- NAVIGASI ---
    implementation(libs.navigation.compose) // Untuk mengatur alur antar halaman [cite: 486]

    // --- VIEWMODEL & LIFECYCLE (MVVM) ---
    implementation(libs.lifecycle.viewmodel.compose) // [cite: 536, 562]
    implementation(libs.lifecycle.runtime.compose)

    // --- DATABASE LOKAL (ROOM - HYBRID) ---
    implementation(libs.bundles.room) // Menggunakan bundle yang kita buat di toml [cite: 301]
    ksp(libs.room.compiler) // Processor untuk Room

    // --- NETWORKING (MYSQL - HYBRID) ---
    implementation(libs.retrofit.serialization) // Converter JSON untuk Retrofit [cite: 306, 307]
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging) // Penting untuk melihat log data MySQL

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}