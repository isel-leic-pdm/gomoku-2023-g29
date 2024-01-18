plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

android {
    namespace = "ipl.isel.daw.gomoku"
    compileSdk = 34

    defaultConfig {
        applicationId = "ipl.isel.daw.gomoku"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "ipl.isel.daw.gomoku.tictactoe.GomokuTestRunner"
        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE**}"
        }
    }

    testOptions {
        packaging {
            jniLibs {
                useLegacyPackaging = true
            }
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE**}"
            }
        }
    }
}

val composeUiVersion = "1.5.4"

dependencies {

    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation ("androidx.activity:activity-compose:1.8.2")


    implementation("androidx.compose.ui:ui:$composeUiVersion")
    implementation("androidx.compose.ui:ui-graphics:$composeUiVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeUiVersion")
    implementation("androidx.compose.material:material:$composeUiVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeUiVersion")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("androidx.compose.foundation:foundation:$composeUiVersion")
    implementation(files("libs/Gomoku.main.jar"))
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    testImplementation ("junit:junit:4.13.2")
    testImplementation ("com.squareup.okhttp3:mockwebserver:4.12.0")


    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation ("io.mockk:mockk-android:1.13.9")

    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")

    androidTestImplementation ("androidx.test:core:1.5.0")
    androidTestImplementation ("androidx.test:core-ktx:1.5.0")

    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:$composeUiVersion")
    debugImplementation ("androidx.compose.ui:ui-tooling:$composeUiVersion")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:$composeUiVersion")
}
