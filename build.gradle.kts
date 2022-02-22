buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Dependencies.Android.Tools.Build.GRADLE)
        classpath(Dependencies.Jetbrains.Kotlin.KOTLIN_GRADLE_PLUGIN)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}