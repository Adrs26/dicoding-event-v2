// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.dynamic.feature) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    //noinspection GradleDependency
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}