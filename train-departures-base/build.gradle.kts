import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    kotlin("jvm")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {

    val ktorVersion = "1.1.1"

    implementation(kotlin("stdlib"))

    // https://ktor.io/clients/http-client.html
    implementation("io.ktor","ktor-client-core", ktorVersion)

    // https://ktor.io/clients/http-client/engines.html
    implementation("io.ktor", "ktor-client-android", ktorVersion)

    // XML - https://developer.android.com/training/basics/network-ops/xml

    // https://github.com/kotlintest/kotlintest/blob/master/doc/reference.md
    testImplementation("io.kotlintest", "kotlintest-runner-junit5", "3.1.11")
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
