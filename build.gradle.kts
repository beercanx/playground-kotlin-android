buildscript {

    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }

    // Review these on each update of the AGP (com.android.application)
    gradle.extra["securityBoms"] = listOf(
        "io.netty:netty-bom:4.2.11.Final",
    )
    gradle.extra["securityPatches"] = listOf(
        "org.apache.httpcomponents:httpmime:4.5.14",
        "org.apache.httpcomponents:httpclient:4.5.14",
        "org.apache.commons:commons-compress:1.28.0",
        "org.apache.commons:commons-lang3:3.20.0",
        "com.google.protobuf:protobuf-java:4.33.5",
        "com.google.protobuf:protobuf-kotlin:4.33.5",
        "org.jdom:jdom2:2.0.6.1",
        "org.bitbucket.b_c:jose4j:0.9.6",
    )

    dependencies {
        classpath("com.android.tools.build:gradle:9.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.0")

        for (securityBom in gradle.extra["securityBoms"] as List<*>) {
            classpath(platform(securityBom!!))
        }
        constraints {
            for (securityPatch in gradle.extra["securityPatches"] as List<*>) {
                classpath(securityPatch!!)
            }
        }
    }
}

allprojects {

    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }

    configurations.named { it.startsWith("_internal-unified-test-platform") }.configureEach {
        // Handles the patching of the Android UTP (Unified Test Platform)
        dependencies {
            for (securityBom in gradle.extra["securityBoms"] as List<*>) {
                add(name, platform(securityBom!!))
            }
            constraints {
                for (securityPatch in gradle.extra["securityPatches"] as List<*>) {
                    add(name, securityPatch!!)
                }
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory.get().asFile)
}
