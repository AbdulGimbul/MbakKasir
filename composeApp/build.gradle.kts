import com.codingfeline.buildkonfig.compiler.FieldSpec
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.realm.plugin)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.detekt)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.core.splashscreen)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)
            implementation(libs.mongodb.realm)
            implementation(libs.kotlin.coroutines)
            implementation(libs.stately.common)
            implementation(libs.kmp.date.time.picker)
            implementation(libs.compottie)
            implementation(libs.compottie.dot)
            api(libs.androidx.datastore)
            api(libs.androidx.datastore.preferences)
            implementation(libs.androidx.paging.runtime)
            implementation(libs.androidx.paging.compose)
            implementation(libs.bundles.ktor)
            implementation(libs.messagebarkmp)
            implementation(libs.qrkit)
            implementation(libs.compose.multiplatform.screen.capture)
            api(libs.gitlive.firebase.kotlin.crashlytics)
//            api(libs.gitlive.firebase.kotlin.config)
            implementation(libs.konnectivity)
            implementation(libs.blue.falcon)
            api(libs.moko.permissions)
            api(libs.moko.permissions.compose)
            implementation(libs.navigation.compose)
            implementation(libs.material.navigation)
//            implementation(libs.material.window.size)
            implementation(libs.adaptive)
            implementation(libs.adaptive.layout)
            implementation(libs.adaptive.navigation)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.material3.window.size.multiplatform)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

buildkonfig {
    packageName = "dev.mbakasir.com"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "dev.mbakasir.com")
    }

    targetConfigs("prod") {
        create("android") {
            buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "mbakasir.com")
        }
        create("ios") {
            buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "mbakasir.com")
        }
    }

    targetConfigs("dev") {
        create("android") {
            buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "dev.mbakasir.com")
        }
        create("ios") {
            buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "dev.mbakasir.com")
        }
    }
}

android {
    namespace = "dev.mbakasir.com"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "dev.mbakasir.com"
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
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

tasks.withType<Detekt>().configureEach {
    reports {
        md.required.set(true)
    }
}
