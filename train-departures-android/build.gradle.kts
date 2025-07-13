plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdk = 35
    namespace = "uk.co.baconi.pka.td"
    defaultConfig {
        applicationId = "uk.co.baconi.pka.td"
        minSdk = 22
        targetSdk = 35
        versionCode = 2
        versionName = "1.1"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    androidExtensions {
        isExperimental = true
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = true // runProguard
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions {
        resources {
            excludes += setOf("META-INF/common.kotlin_module", "META-INF/*.kotlin_module")
        }
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    // Disabled because adding in an MMP module manages to break the lint tasks
    tasks["lint"].enabled = false
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))

    implementation(project(":train-departures-common"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.2.2")

    implementation("io.ktor:ktor-client-okhttp:1.1.3")

    implementation("androidx.core:core-ktx:1.0.2")
    implementation("androidx.preference:preference-ktx:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.0.0")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("com.google.android.material:material:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("androidx.annotation:annotation:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    testImplementation("junit:junit:4.12")

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
