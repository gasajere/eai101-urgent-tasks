// Week 1 of the morning course: plain Kotlin on the JVM, no Android.
// Versions deliberately match week-02-rick-and-morty/gradle/libs.versions.toml where they overlap.
plugins {
    kotlin("jvm") version "2.1.0"
}

group = "com.aac.training"
version = "0.1"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
}

// One task per lesson so interns can run exactly today's example:
//   .\gradlew.bat day1   (Windows)      ./gradlew day1   (macOS/Linux)
// Day 5 is the checkpoint; its behaviour is also covered by `.\gradlew.bat test`.
listOf(1, 2, 3, 4, 5).forEach { day ->
    tasks.register<JavaExec>("day$day") {
        group = "lessons"
        description = "Run the Day $day example"
        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("week01.Day${day}Kt")
        javaLauncher.set(javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(21)) })
    }
}

tasks.test {
    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = true
    }
}
