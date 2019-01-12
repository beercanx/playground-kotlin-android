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
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.6"
    }
}
