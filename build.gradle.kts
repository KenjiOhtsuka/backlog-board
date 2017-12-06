import org.gradle.api.*
import org.gradle.kotlin.dsl.*
import org.gradle.plugin.*
import org.gradle.script.lang.kotlin.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.improve_future"
version = "1.0-SNAPSHOT"

buildscript {
    val kotlinVersion = "1.1.61"
    extra["kotlin_version"] = kotlinVersion

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
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
    compile("org.springframework.boot:spring-boot-starter-web:1.5.8.RELEASE")

    // 追加部分
    compile("org.springframework.boot:spring-boot-starter-data-jpa")
    compile("org.postgresql:postgresql:42.1.4")

    val kotlinxHtmlVersion = "0.6.6"
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")

}

tasks.withType<KotlinCompile>(KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
