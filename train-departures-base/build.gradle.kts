import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(22)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk7"))

    // https://github.com/arrow-kt/arrow
    implementation("io.arrow-kt:arrow-core:0.8.2")

    // https://ktor.io/clients/http-client.html
    implementation("io.ktor:ktor-client-core:1.2.1")

    // https://ktor.io/clients/http-client/engines.html
    implementation("io.ktor:ktor-client-android:1.2.1")

    // XML - https://developer.android.com/training/basics/network-ops/xml

    // https://github.com/kotlintest/kotlintest/blob/master/doc/reference.md
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")

    // XmlPull impl
    testImplementation("net.sf.kxml:kxml2:2.3.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.6"
    }
}
