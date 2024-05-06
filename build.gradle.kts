plugins {
    kotlin("jvm") version "1.9.23"
}

group = "media.togi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin-bundle:6.1.3")
    implementation("com.aallam.openai:openai-client:3.7.2")
    implementation("io.ktor:ktor-client-java:2.3.10")
    implementation("io.ktor:ktor-serialization-jackson:2.3.10")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.10")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
