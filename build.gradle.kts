plugins {
    kotlin("jvm") version "1.9.21"
    application
}


group = "org.example"
version = "1.0-SNAPSHOT"


application {
    mainClass.set("org.example.MainKt")
}

repositories {
    mavenCentral()
}

val ktor_version: String = "2.3.7"

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}


