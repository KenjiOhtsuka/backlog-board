import org.gradle.api.*
import org.gradle.kotlin.dsl.*
import org.gradle.plugin.*
import org.gradle.script.lang.kotlin.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.improve_future"
version = "1.0-SNAPSHOT"

buildscript {
    val kotlinVersion = "1.2.0"
    extra["kotlin_version"] = kotlinVersion

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    }
}

plugins {
    id("org.springframework.boot") version "1.5.8.RELEASE"
}

apply {
    plugin("kotlin")
    plugin("war")
}


repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    val kotlinVersion = extra["kotlin_version"] as String
    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")
    testCompile("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testCompile("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    compile("org.springframework.boot:spring-boot-starter-web:1.5.8.RELEASE")
    compile("org.springframework.boot:spring-boot-starter-security")
    // https://mvnrepository.com/artifact/org.json/json
    compile("org.json:json:20160810")

    val kotlinxHtmlVersion = "0.6.6"
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")

    compile("com.nulab-inc:backlog4j:2.2.0")
}

tasks.withType<KotlinCompile>(KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType(org.gradle.language.jvm.tasks.ProcessResources::class.java) {
    this.filter(
            mapOf(
                    "tokens" to mapOf(
                            "activeProfile" to (project.properties["activeProfile"] ?: "default")
                    )
            ),
            org.apache.tools.ant.filters.ReplaceTokens::class.java
    )
}
