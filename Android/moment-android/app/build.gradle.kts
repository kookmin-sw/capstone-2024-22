

plugins {
    kotlin("plugin.serialization") version "1.5.0"
    kotlin("kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")


}

android {
    namespace = "com.capstone.android.application"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.capstone.android.application"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }



    signingConfigs {
        create("release") {
            keyAlias = "moment-release-key2"
            keyPassword = "rlaalswnd1"
            storeFile = file("/Users/kimminjung/AndroidStudioProjects/moment-key-manage/release/moment-releasekeystore2")
            storePassword = "rlaalswnd1"
        }
    }

    buildTypes {

//        배포 준비 할 때 디버그 모드와 릴리즈 모드 나누기
//        debug {
//            applicationIdSuffix = ".debug"
//            isDebuggable = true
//
//        }

        release {

            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kapt {
        correctErrorTypes = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        this.compose = true
        this.viewBinding = true
        this.dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


}


dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.compose.material3:material3-android:1.2.1")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.google.android.gms:play-services-wearable:18.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Gson Converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //logging-interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // ViewPager with Compose
    implementation("com.google.accompanist:accompanist-pager:0.20.1")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.20.1")

    // Navigation
    val nav_version = "2.4.2"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    val room_version = "2.5.0"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    val hilt_version = "2.44"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")

    implementation("io.woong.compose.grid:grid:1.2.2")

    // swipe 구현을 위한 라이
    implementation("androidx.compose.foundation:foundation:1.6.0-alpha04")

    // coroutine 의존성
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    // enable constraintlayout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    // AsyncImage
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Glide-Compose
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    // firebase
    implementation("com.google.firebase:firebase-messaging:21.1.0")

}
