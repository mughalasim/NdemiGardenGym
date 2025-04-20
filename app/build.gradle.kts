import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.VariantDimension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kapt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
}

apply(from = "${rootProject.projectDir}/lint.gradle")

val keystorePropertiesFile = rootProject.file("keystore.properties")
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

        setConfigVariable(variableName = "CURRENCY_CODE", variableSource = "CURRENCY_CODE")
        setConfigVariable(variableName = "API_BASE_URL", variableSource = "API_BASE_URL")
        setConfigVariable(variableName = "ADMIN_STAGING", variableSource = "ADMIN_STAGING")
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
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_21.toString()
    }

    flavorDimensions += "version"
    productFlavors {
        create("Live") {
            dimension = "version"
            resValue("string", "app_name", libs.versions.appName.get())
            setConfigVariable(variableName = "PATH_USER", variableSource = "PATH_USER")
            setConfigVariable(variableName = "PATH_ATTENDANCE", variableSource = "PATH_ATTENDANCE")
            setConfigVariable(
                variableName = "PATH_PAYMENT_PLAN",
                variableSource = "PATH_PAYMENT_PLAN",
            )
            setConfigVariable(
                variableName = "PATH_APP_VERSION_TYPE",
                variableSource = "PATH_APP_VERSION_TYPE",
            )
            setConfigVariable(
                variableName = "PATH_USER_IMAGES",
                variableSource = "PATH_USER_IMAGES",
            )
            setProperty(
                "archivesBaseName",
                "${libs.versions.appName.get()} (${libs.versions.appVersionName.get()})",
            )
        }

        create("Staging") {
            isDefault = true
            dimension = "version"
            applicationIdSuffix = ".staging"
            resValue("string", "app_name", "${libs.versions.appName.get()} (Staging)")
            setConfigVariable(variableName = "PATH_USER", variableSource = "DEBUG_PATH_USER")
            setConfigVariable(
                variableName = "PATH_ATTENDANCE",
                variableSource = "DEBUG_PATH_ATTENDANCE",
            )
            setConfigVariable(
                variableName = "PATH_PAYMENT_PLAN",
                variableSource = "DEBUG_PATH_PAYMENT",
            )
            setConfigVariable(
                variableName = "PATH_APP_VERSION_TYPE",
                variableSource = "DEBUG_PATH_APP_VERSION_TYPE",
            )
            setConfigVariable(
                variableName = "PATH_USER_IMAGES",
                variableSource = "DEBUG_PATH_USER_IMAGES",
            )
            setProperty(
                "archivesBaseName",
                "${libs.versions.appName.get()} (${libs.versions.appVersionName.get()})",
            )
        }
    }

    kapt {
        correctErrorTypes = true
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

    // Navigation
    implementation(libs.navigation.compose)

    // Koin Dependency Injection
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    // TESTING -------------------------------------------------------------------------------------
    testImplementation(libs.junit)
}
