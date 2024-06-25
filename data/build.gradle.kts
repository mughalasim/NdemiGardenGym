plugins {
    alias(libs.plugins.kapt)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin.serialization)
}

android {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    namespace = "cv.data"
    compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

    defaultConfig {
        minSdk = libs.findVersion("minSdk").get().toString().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    detekt {
        toolVersion = libs.findVersion("detekt").get().toString()
        config.setFrom(rootProject.file("detekt.yml"))
        buildUponDefaultConfig = true
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(libs.core.ktx)

    api(libs.retrofit)
    api(libs.retrofit2.kotlinx.serialization.converter)
    api(libs.kotlinx.serialization.json)

    api(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    api(libs.logging.interceptor)

    api(platform(libs.firebase.bom))
    api(libs.firebase.auth)
    api(libs.firebase.storage)
    api(libs.firebase.firestore)
    api(libs.firebase.analytics)
    api(libs.firebase.crashlytics)

    // android x keep annotation
    implementation(libs.annotation.jvm)

    // Joda time
    api(libs.android.joda)

    // Testing -------------------------------------------------------------------------------------
    testImplementation(libs.junit)
}
