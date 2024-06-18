import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kapt)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin.serialization)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val appName = libs.findVersion("appName").get().toString()
    val versionNameString = libs.findVersion("appVersionName").get().toString()

    namespace = libs.findVersion("appNamespaceId").get().toString()
    compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

    defaultConfig {
        applicationId = libs.findVersion("appNamespaceId").get().toString()
        minSdk = libs.findVersion("minSdk").get().toString().toInt()
        targetSdk = libs.findVersion("compileSdk").get().toString().toInt()
        versionCode = libs.findVersion("appVersionCode").get().toString().toInt()
        versionName = versionNameString
        buildConfigField(
            "String",
            "API_BASE_URL",
            gradleLocalProperties(rootDir, providers).getProperty("API_BASE_URL"),
        )
        buildConfigField(
            "String",
            "ADMIN_LIVE",
            gradleLocalProperties(rootDir, providers).getProperty("ADMIN_LIVE"),
        )
        buildConfigField(
            "String",
            "ADMIN_STAGING",
            gradleLocalProperties(rootDir, providers).getProperty("ADMIN_STAGING"),
        )
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
            resValue("string", "app_name", appName)
            buildConfigField(
                "String",
                "PATH_USER",
                gradleLocalProperties(rootDir, providers).getProperty("PATH_USER"),
            )
            buildConfigField(
                "String",
                "PATH_ATTENDANCE",
                gradleLocalProperties(rootDir, providers).getProperty("PATH_ATTENDANCE"),
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            resValue("string", "app_name", "$appName (Debug)")
            buildConfigField(
                "String",
                "PATH_USER",
                gradleLocalProperties(rootDir, providers).getProperty("DEBUG_PATH_USER"),
            )
            buildConfigField(
                "String",
                "PATH_ATTENDANCE",
                gradleLocalProperties(rootDir, providers).getProperty("DEBUG_PATH_ATTENDANCE"),
            )
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.findVersion("compiler").get().toString()
    }

    flavorDimensions += "version"
    productFlavors {
        create("App") {
            dimension = "version"
            setProperty("archivesBaseName", "$appName ($versionNameString)")
        }
    }

    kapt {
        correctErrorTypes = true
    }

    firebaseCrashlytics {
        mappingFileUploadEnabled = false
        nativeSymbolUploadEnabled = false
    }

    detekt {
        toolVersion = libs.findVersion("detekt").get().toString()
        config.setFrom(rootProject.file("detekt.yml"))
        buildUponDefaultConfig = true
    }

    packaging {
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/NOTICE.txt")
    }
}

dependencies {

    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.kotlin.reflect)
    implementation(libs.core.ktx)

    implementation(libs.compiler)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3.android)
    debugImplementation(libs.ui.tooling)
    implementation(libs.material)
    implementation(libs.runtime.livedata)
    implementation(libs.foundation.android)

    implementation(libs.activity.compose)
    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.savedstate)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.extensions)

    // Navigation
    implementation(libs.navigation.compose)

    // Koin Dependency Injection
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)

    // Restring for Dynamic languages
    implementation(libs.restring)
    implementation(libs.viewpump)
    implementation(libs.reword)
    implementation(libs.applocale)
    implementation(libs.appcompat)


    // TESTING -------------------------------------------------------------------------------------
    testImplementation(libs.junit)
}
