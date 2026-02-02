rootProject.name = "mpfocus"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")

// HOTest is under heavy and simultaneous development with MultiProjectFocus [MPF]
// thus now - MPF depends directly on HOT sources, not built components like jars etc
includeBuild("../HOTest")
// includeBuild understands relative and absolut paths eg
// """C:\workspace\myproject123\HOTest"""
// """/home/users/user123/myprojects/HOTest"""

