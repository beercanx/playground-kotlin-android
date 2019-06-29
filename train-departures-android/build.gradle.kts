plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "uk.co.baconi.pka.td"
        minSdkVersion(22)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    lintOptions {
        isCheckReleaseBuilds = false
    }
    androidExtensions {
        isExperimental = true
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isZipAlignEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = true // runProguard
            isZipAlignEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions {
        exclude("META-INF/common.kotlin_module")
        exclude("META-INF/*.kotlin_module")
    }
}

dependencies {

    implementation(project(":train-departures-base"))

    // https://github.com/arrow-kt/arrow
    implementation("io.arrow-kt:arrow-core:0.8.2") // TODO - Work out why its having to be manually added

    implementation(kotlin("stdlib-jdk7"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.2.1")

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
