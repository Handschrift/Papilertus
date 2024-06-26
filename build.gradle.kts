import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "org.papilertus"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.dv8tion:JDA:5.0.0-beta.21")
    implementation("com.github.MinnDevelopment:jda-reactor:1.5.0")
    implementation("com.sksamuel.hoplite:hoplite-core:2.7.3")
    implementation("com.sksamuel.hoplite:hoplite-toml:2.7.3")
    implementation("com.github.minndevelopment:jda-ktx:78dbf827d51fd314194688425e193758c77dfbe1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.litote.kmongo:kmongo:4.9.0")
    implementation("org.mongodb:mongodb-driver-sync:4.9.1")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("com.github.ygimenez:Pagination-Utils:4.0.6")
}

application {
    mainClass.set("com.papilertus.init.PapilertusKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.papilertus.init.PapilertusKt"
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    // We have to exclude these because of this (annoying) bug: https://youtrack.jetbrains.com/issue/KT-25709/IDE-Unresolved-reference-from-fat-jar-dependency-with-Kotlin-runtime
    exclude("**/*.kotlin_metadata")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_21.toString()
}