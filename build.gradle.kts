buildscript {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.11.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.0")
    }
}

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory.get().asFile)
}
