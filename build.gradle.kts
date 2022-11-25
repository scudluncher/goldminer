import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.6.21"
}

java.sourceCompatibility = JavaVersion.VERSION_17

allprojects {
    group = "com.carrera"
    version = "0.0.1-SNAPSHOT"


    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
}

extra["snippetsDir"] = file("build/generated-snippets")

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


