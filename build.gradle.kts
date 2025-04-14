plugins {
    kotlin("jvm") version "2.1.10"
    application
}


group = "fr.axxonte"
version = "1.0-SNAPSHOT"


application {
    mainClass.set("org.example.MainKt")
}

repositories {
    mavenCentral()
}

val ktor_version: String = "3.1.1"

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")

    implementation("com.github.oshi:oshi-core:6.4.13")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
