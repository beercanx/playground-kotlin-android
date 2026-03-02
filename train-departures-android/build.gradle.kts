plugins {
    id("com.android.application")
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
        targetSdk = 36
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = true // runProguard
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.ktor.client.okhttp)

    implementation(libs.core.ktx)
    implementation(libs.preference.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.recyclerview)
    implementation(libs.annotation)
    implementation(libs.constraintlayout)
    implementation(libs.swiperefreshlayout)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.espresso.core)
}
