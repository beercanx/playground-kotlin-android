import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
}

kotlin {

    jvm("jvm") {
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
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(kotlin("reflect"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.5")

                implementation("io.ktor:ktor-client-core:1.1.3")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.mockk:mockk-common:1.9.3")
            }
        }

        jvm().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")

                implementation("io.ktor:ktor-client-core-jvm:1.1.3")

                // XML - https://developer.android.com/training/basics/network-ops/xml
                compileOnly("net.sf.kxml:kxml2:2.3.0")
            }
        }

        jvm().compilations["test"].defaultSourceSet {
            dependencies {

                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))

                implementation("io.mockk:mockk:1.9.3")

                // https://github.com/kotlintest/kotlintest/blob/master/doc/reference.md
                implementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")

                // Adding support for running junit4
                runtimeOnly("org.junit.vintage:junit-vintage-engine:5.4.2")

                // XmlPull impl
                implementation("net.sf.kxml:kxml2:2.3.0")
            }
        }

        // TODO - Add iOS support at some point
        //iosMain {
        //    dependencies {
        //        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.2.2")
        //
        //        implementation("io.ktor:ktor-client-core-native:1.1.3")
        //        implementation("io.ktor:ktor-client-ios:1.1.3")
        //    }
        //}
    }
}