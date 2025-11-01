import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("multiplatform")
}

kotlin {

    jvmToolchain(11)

    jvm("jvm") {
        tasks.withType<Test> {
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
                exceptionFormat = TestExceptionFormat.FULL
            }
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(kotlin("reflect"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.8")

                implementation("io.ktor:ktor-client-core:3.3.1")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.mockk:mockk:1.14.5")
                implementation("io.ktor:ktor-client-mock:3.3.1")
            }
        }

        jvm().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

                implementation("io.ktor:ktor-client-core-jvm:3.3.1")

                // XML - https://developer.android.com/training/basics/network-ops/xml
                compileOnly("net.sf.kxml:kxml2:2.3.0")
            }
        }

        jvm().compilations["test"].defaultSourceSet {
            dependencies {

                implementation(kotlin("test"))
                implementation(kotlin("test-junit5"))

                implementation("io.mockk:mockk:1.14.5")

                // https://github.com/kotlintest/kotlintest/blob/master/doc/reference.md
//                implementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")

                // Adding support for running junit4
//                runtimeOnly("org.junit.vintage:junit-vintage-engine:5.12.2")

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