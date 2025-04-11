plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.kanawish.sample.hello"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.kanawish.sample.hello"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    android.sourceSets {
        getByName("main") {
            java.srcDir(File("build/generated/ksp/main/kotlin"))
        }
    }
}

/* Koin KSP is not really used for now.
ksp {
    arg("KOIN_CONFIG_CHECK","true")
}
*/

dependencies {
//    implementation("com.hardkernel:driver-Mcp2515:0.5.3")
    implementation("com.google.android.things:androidthings:1.0")
    implementation("com.google.android.things.contrib:driver-rainbowhat:1.0")

    implementation("com.google.android.things.contrib:driver-apa102:1.0")
    implementation("com.google.android.things.contrib:driver-button:1.0")
    implementation("com.google.android.things.contrib:driver-bmx280:1.0")
    implementation("com.google.android.things.contrib:driver-ht16k33:1.0")
    implementation("com.google.android.things.contrib:driver-pwmspeaker:1.0")
    implementation("com.google.android.things.contrib:driver-pwmservo:1.0")

    // NOTE: Looks like 'api' weren't folded in on local release, tbd why...
    implementation("com.google.mlkit:barcode-scanning:17.0.2")

    implementation(libs.accompanist.permissions)

    implementation(platform(libs.androidx.compose.bom.v20240901))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.compose.material)
    implementation(libs.androidx.material.navigation)

    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    implementation(platform(libs.coil.bom))
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Koin KSP is not really used for now.
//    implementation(libs.koin.annotations)
//    ksp(libs.koin.ksp.compiler)

    implementation(libs.konfetti.compose)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.livekit.android.compose.components)

    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.square.okhttp3.loggingInterceptor)
    implementation(libs.square.retrofit)
    implementation(libs.square.retrofit.converterGson)
    implementation(libs.okio)

    implementation(libs.usbSerialForAndroid)

    // Mordant is a multiplatform library for rendering styled text in the terminal.
    // see https://ajalt.github.io/mordant/
    implementation(libs.mordant)
    implementation(libs.mordant.coroutines)
    implementation(libs.mordant.markdown)

    implementation(libs.timberkt)

    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)

    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(kotlin("test"))

    androidTestImplementation(platform(libs.androidx.compose.bom.v20240901))
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.compose.ui.tooling)
}