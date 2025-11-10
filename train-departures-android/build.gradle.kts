plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}

kotlin {
    jvmToolchain(11)
}

android {
    compileSdk = 36
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
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
    packaging {
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

    implementation(project(":train-departures-common"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    implementation("io.ktor:ktor-client-okhttp:3.3.2")

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.fragment:fragment-ktx:1.8.9")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test:runner:1.7.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}
