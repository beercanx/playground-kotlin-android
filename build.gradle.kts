buildscript {

    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }

    // Review these on each update of the AGP (com.android.application)
    gradle.extra["securityBoms"] = listOf(
        "org.bouncycastle:bc-jdk18on-bom:1.84",
        "io.netty:netty-bom:4.2.15.Final",
    )
    gradle.extra["securityPatches"] = listOf(
        "org.apache.httpcomponents:httpmime:4.5.14",
        "org.apache.httpcomponents:httpclient:4.5.14",
        "org.apache.commons:commons-compress:1.28.0",
        "org.apache.commons:commons-lang3:3.20.0",
        "com.google.protobuf:protobuf-java:4.35.0",
        "com.google.protobuf:protobuf-kotlin:4.35.0",
        "org.jdom:jdom2:2.0.6.1",
        "org.bitbucket.b_c:jose4j:0.9.6",
    )

    dependencies {
        classpath("com.android.tools.build:gradle:9.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.0")

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

    // Handles the patching of the Android UTP (Unified Test Platform) and Android Lint
    configurations.named { it.startsWith("unified-test-platform") || it == "androidLintTool" }.configureEach {
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
