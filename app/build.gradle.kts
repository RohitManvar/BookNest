plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.virtuallibrary"
    compileSdk = 36  // Changed from 36 to 34 (stable version)

    defaultConfig {
        applicationId = "com.example.virtuallibrary"
        minSdk = 24
        targetSdk = 36 // Changed from 36 to 34 (stable version)
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
    buildToolsVersion = "36.0.0"
    ndkVersion = "29.0.13599879 rc2"

    configurations.all {
        resolutionStrategy {
            force("androidx.core:core:1.13.0")
            force("androidx.appcompat:appcompat:1.6.1")
        }
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    // Remove duplicate appcompat declaration
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.preference)
    implementation(libs.animated.vector.drawable)
    implementation(libs.recyclerview)
    implementation(libs.cardview)
    implementation("androidx.core:core:1.13.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Use only one Glide declaration (prefer catalog version)
    implementation(libs.glide)
    implementation(libs.room.common.jvm)
    implementation(libs.firebase.firestore)
    implementation(libs.room.runtime.jvm)
    implementation(libs.firebase.database)
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}