import org.gradle.api.tasks.Exec
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "pl.cube.planning_poker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "pl.cube.planning_poker"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.ui.tooling)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use(::load)
    }
}

val adbExecutable = run {
    val sdkDir = localProperties.getProperty("sdk.dir")
        ?: System.getenv("ANDROID_SDK_ROOT")
        ?: System.getenv("ANDROID_HOME")
    if (sdkDir == null) {
        null
    } else {
        val adbName = if (System.getProperty("os.name").startsWith("Windows", ignoreCase = true)) {
            "adb.exe"
        } else {
            "adb"
        }
        file("$sdkDir/platform-tools/$adbName").takeIf { it.exists() }
    }
}

val adbReverseLocalBackend by tasks.registering(Exec::class) {
    group = "development"
    description = "Expose local backend to Android debug app via adb reverse."

    val adb = adbExecutable
    if (adb == null) {
        enabled = false
        doFirst {
            logger.warn("Skipping adb reverse: adb executable not found.")
        }
    } else {
        commandLine(adb.absolutePath, "reverse", "tcp:8080", "tcp:8080")
        isIgnoreExitValue = true
    }
}

tasks.matching { it.name == "installDebug" }.configureEach {
    dependsOn(adbReverseLocalBackend)
}
