import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val title = "Robocode Tank Royale GUI"
description = "Desktop application for Robocode Tank Royale"

group = "dev.robocode.tankroyale"
val artifactId = "robocode-tankroyale-gui"
version = "0.8.3"


val serverVersion = "0.8.10"
val bootstrapVersion = "0.8.0"


plugins {
    `java-library`
    kotlin("jvm") version "1.5.20-RC"
    kotlin("plugin.serialization") version "1.5.20-RC"
    `maven-publish`
    idea
    id("com.github.ben-manes.versions") version "0.39.0"
}

tasks.withType<KotlinCompile> {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

idea {
    module {
        outputDir = file("$buildDir/classes/kotlin/main")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.20-RC")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")

    implementation("com.miglayout:miglayout-swing:11.0")

    runtimeOnly("dev.robocode.tankroyale:robocode-tankroyale-server:${serverVersion}") {
        exclude("ch.qos.logback")
    }
    runtimeOnly("dev.robocode.tankroyale:robocode-tankroyale-bootstrap:${bootstrapVersion}")
}

val copyServerJar = task<Copy>("copyServerJar") {
    from(configurations.runtimeClasspath)
    into(idea.module.outputDir)
    include("robocode-tankroyale-server-*.jar")
    rename("(.*)-[0-9]+\\..*.jar", "\$1.jar")
}

val copyBootstrapJar = task<Copy>("copyBootstrapJar") {
    from(configurations.runtimeClasspath)
    into(idea.module.outputDir)
    include("robocode-tankroyale-bootstrap-*.jar")
    rename("(.*)-[0-9]+\\..*.jar", "\$1.jar")
}

tasks.processResources {
    with(copySpec {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from("/src/main/resources")
        include("version.txt")
        filter(ReplaceTokens::class, "tokens" to mapOf("version" to version))
    })
}

val fatJar = task<Jar>("fatJar") {
    manifest {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        attributes["Implementation-Title"] = title
        attributes["Implementation-Version"] = archiveVersion
        attributes["Main-Class"] = "dev.robocode.tankroyale.gui.ui.MainWindowKt"
    }
    from(
        configurations.compileClasspath.get().filter { it.name.endsWith(".jar") }.map { zipTree(it) },
        configurations.runtimeClasspath.get().filter { it.name.endsWith(".jar") }.map { zipTree(it) }
    )
    exclude("*.kotlin_metadata")
    with(tasks["jar"] as CopySpec)
    duplicatesStrategy = DuplicatesStrategy.WARN
}

tasks.named("build") {
    dependsOn(copyServerJar)
    dependsOn(copyBootstrapJar)
//    dependsOn(fatJar)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(fatJar)
            groupId = group as String?
            artifactId
            version
        }
    }
}

