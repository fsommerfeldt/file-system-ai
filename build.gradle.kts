plugins {
    kotlin("jvm") version "2.1.10"
}

group = "ch.fsommerfeldt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.langchain4j:langchain4j-ollama:1.0.0-beta1")
    implementation("dev.langchain4j:langchain4j-easy-rag:1.0.0-beta1")
    implementation("ch.qos.logback:logback-classic:1.5.8")
    implementation("org.json:json:20250107")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}