import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.VariantDimension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
}

apply(from = "${rootProject.projectDir}/lint.gradle")

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = libs.versions.appNamespaceId.get()
    compileSdk =
        libs.versions.appCompileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = libs.versions.appNamespaceId.get()
        minSdk =
            libs.versions.appMinSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.appTargetSdk
                .get()
                .toInt()
        versionCode =
            libs.versions.appVersionCode
                .get()
                .toInt()
        versionName = libs.versions.appVersionName.get()

        setConfigVariable(variableName = "API_BASE_URL", variableSource = "API_BASE_URL")
        setConfigVariable(variableName = "PATH_PAYMENT", variableSource = "PATH_PAYMENT")
        setConfigVariable(variableName = "PATH_APP_VERSION", variableSource = "PATH_APP_VERSION")

        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        vectorDrawables { useSupportLibrary = true }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("config") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("config")
        }

        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    flavorDimensions += "version"
    productFlavors {
        create("Live") {
            dimension = "version"

            resValue("string", "app_name", libs.versions.appName.get())

            setConfigVariable("PATH_USER", "PATH_USER")
            setConfigVariable("PATH_ATTENDANCE", "PATH_ATTENDANCE")
            setConfigVariable("PATH_PAYMENT_PLAN", "PATH_PAYMENT_PLAN")
            setConfigVariable("PATH_APP_VERSION_TYPE", "PATH_APP_VERSION_TYPE")
            setConfigVariable("PATH_USER_IMAGES", "PATH_USER_IMAGES")
            setConfigVariable("PATH_WEIGHT", "PATH_WEIGHT")
            setConfigVariable("PATH_TRACKED", "PATH_TRACKED")

            setConfigVariable("EMAIL_ADMIN", "EMAIL_ADMIN_LIVE")
            setConfigVariable("EMAIL_SUPER_ADMIN", "EMAIL_SUPER_ADMIN_LIVE")
            setConfigVariable("EMAIL_SUPERVISOR", "EMAIL_SUPERVISOR_LIVE")
            setConfigVariable("EMAIL_MEMBER", "EMAIL_MEMBER_LIVE")
            setConfigVariable("TEST_PASS", "EMPTY")
        }

        create("Staging") {
            isDefault = true
            dimension = "version"
            applicationIdSuffix = ".staging"

            resValue("string", "app_name", "${libs.versions.appName.get()} (Staging)")

            setConfigVariable("PATH_USER", "DEBUG_PATH_USER")
            setConfigVariable("PATH_ATTENDANCE", "DEBUG_PATH_ATTENDANCE")
            setConfigVariable("PATH_PAYMENT_PLAN", "DEBUG_PATH_PAYMENT")
            setConfigVariable("PATH_APP_VERSION_TYPE", "DEBUG_PATH_APP_VERSION_TYPE")
            setConfigVariable("PATH_USER_IMAGES", "DEBUG_PATH_USER_IMAGES")
            setConfigVariable("PATH_WEIGHT", "PATH_WEIGHT")
            setConfigVariable("PATH_TRACKED", "DEBUG_PATH_TRACKED")

            setConfigVariable("EMAIL_ADMIN", "EMAIL_ADMIN_STAGING")
            setConfigVariable("EMAIL_SUPER_ADMIN", "EMAIL_SUPER_ADMIN_STAGING")
            setConfigVariable("EMAIL_SUPERVISOR", "EMAIL_SUPERVISOR_STAGING")
            setConfigVariable("EMAIL_MEMBER", "EMAIL_MEMBER_STAGING")
            setConfigVariable("TEST_PASS", "TEST_PASS")
        }
    }

    firebaseCrashlytics {
        mappingFileUploadEnabled = false
        nativeSymbolUploadEnabled = false
    }

    packaging {
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/NOTICE.txt")
    }
}

fun VariantDimension.setConfigVariable(
    variableName: String,
    variableSource: String,
) {
    buildConfigField(
        "String",
        variableName,
        gradleLocalProperties(rootDir, providers).getProperty(variableSource),
    )
}

fun ApplicationDefaultConfig.setConfigVariable(
    variableName: String,
    variableSource: String,
) {
    buildConfigField(
        "String",
        variableName,
        gradleLocalProperties(rootDir, providers).getProperty(variableSource),
    )
}

dependencies {

    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.core.ktx)

    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)
    implementation(libs.material3.android)
    implementation(libs.material.icons.extended.android)

    implementation(libs.runtime.livedata)
    implementation(libs.foundation.android)

    implementation(libs.activity.compose)

    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.savedstate)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.extensions)

    // Image loading - Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    // Navigation
    implementation(libs.navigation.compose)

    // Koin Dependency Injection
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.javax.inject)

    // Charting library
    implementation(libs.mpandroidchart)

    // TESTING -------------------------------------------------------------------------------------
    testImplementation(libs.junit)
}
