plugins {
    id("com.android.application")
    id("kotlin-android-extensions")
    kotlin("android")
    kotlin("kapt")
    id("com.github.ben-manes.versions") version "0.42.0"
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs")
    id("org.jetbrains.kotlin.plugin.serialization")
}

// Disabling the generating of kotlin synthetics as we use ViewBinding
// https://www.google.com/search?q=androidExtensions+features&rlz=1C5CHFA_enCZ965CZ965&oq=androidExtensions+features&aqs=chrome..69i57j0i22i30j0i8i13i30.1685j0j7&sourceid=chrome&ie=UTF-8
androidExtensions.features = setOf("parcelize")
kapt.correctErrorTypes = true
kotlin {
    experimental {
        coroutines = org.jetbrains.kotlin.gradle.dsl.Coroutines.ENABLE
    }
}
android {
    compileSdk = 31
    val baseAppId = "com.kinzlstanislav.sigyctest"
    defaultConfig {
        applicationId = baseAppId
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        buildConfigField("String", "API_BASE_URL", project.properties["BASE_URL_ALL_ENV"].toString())
        buildConfigField("String", "CAT_API_KEY", project.properties["CAT_API_KEY"].toString())
        buildConfigField("BASE_APP_ID", baseAppId)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

    lintOptions.isAbortOnError = false

    val meta = "META-INF/"
    packagingOptions.jniLibs.excludes.addAll(
        setOf(
            "${meta}DEPENDENCIES", "${meta}LICENSE",
            "${meta}LICENSE.txt", "${meta}license.txt",
            "${meta}NOTICE", "${meta}NOTICE.txt", "${meta}notice.txt",
            "${meta}ASL2.0",
            "${meta}*.kotlin_module"
        )
    )

    buildTypes {
        debug {
            buildConfigField("boolean", "isDebug", true.toString())
            signingConfig = signingConfigs["debug"]
        }
        release {
            isMinifyEnabled = false
            buildConfigField("boolean", "isDebug", false.toString())
            proguardFiles.add(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFiles.add(File("proguard-rules.pro"))
            signingConfig = signingConfigs.create("release")
        }
    }

    val defaultFlavorDimension = "type"
    flavorDimensions.add(defaultFlavorDimension)
    productFlavors {
        create("dev") {
            applicationIdSuffix = ".dev"
            dimension = defaultFlavorDimension
        }
        create("prod") {
            applicationIdSuffix = ".prod"
            dimension = defaultFlavorDimension
        }
    }
    applicationVariants.all {
        sourceSets {
            // For ksp / Koin annotations
            getByName(name).java.srcDirs("build/generated/ksp/${name}/kotlin")
            // For View binding
            getByName(name).java.srcDirs("build/generated/data_binding_base_class_source_out/${name}/out")
        }
    }
}

dependencies {
    //_________CORE Dependencies_____________________________________________
    // -> ./gradlew dependencyUpdates to more properly check for newer versions.
    val kotlinStdLibJdk7 = "1.6.10"
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", kotlinStdLibJdk7))
    // Android core SDK
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.multidex:multidex:2.0.1") // https://developer.android.com/studio/build/multidex
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.window:window:1.0.0")
    // Logging
    implementation("com.jakewharton.timber:timber:4.7.1")

    //_________UI Libs Dependencies_____________________________________________
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("com.google.android:flexbox:2.0.1")
    implementation("androidx.fragment:fragment-ktx:1.4.1")
    // Epoxy
    implementation("com.airbnb.android:epoxy:4.6.3")
    kapt("com.airbnb.android:epoxy-processor:4.6.3")
    // Compose
    /*implementation("androidx.compose.runtime:runtime:1.1.1")
    implementation("androidx.compose.ui:ui:1.1.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.1.1")
    implementation("androidx.compose.foundation:foundation-layout:1.1.1")
    implementation("androidx.compose.material:material:1.1.1")
    implementation("androidx.compose.material:material-icons-extended:1.1.1")
    implementation("androidx.compose.foundation:foundation:1.1.1")
    implementation("androidx.compose.animation:animation:1.1.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.1.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")
    implementation("androidx.compose.material:material-ripple:1.1.1")
    implementation("androidx.compose.animation:animation-core:1.1.1")
    implementation("androidx.compose.animation:animation-graphics:1.1.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.1.1")*/

    //_________OTHER Libs Dependencies_____________________________________________
    //Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    // Workers
    implementation("androidx.work:work-runtime-ktx:2.7.1")
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.4.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.1")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.0")
    // Room
    implementation("androidx.room:room-runtime:2.4.2")
    kapt("androidx.room:room-compiler:2.4.2")
    implementation("androidx.room:room-ktx:2.4.2")
    implementation("androidx.room:room-guava:2.4.2")
    implementation("io.github.kaustubhpatange:autobindings-room:1.1-beta05") // Type converter annotations
    kapt("io.github.kaustubhpatange:autobindings-compiler:1.1-beta05")
    // Proto
    implementation("androidx.datastore:datastore:1.0.0")
    // Koin
    implementation("io.insert-koin:koin-core:3.2.0-beta-1")
    implementation("io.insert-koin:koin-android:3.2.0-beta-1")
//    implementation("io.insert-koin:koin-androidx-compose:3.2.0-beta-1")
    implementation("io.insert-koin:koin-androidx-navigation:3.2.0-beta-1")
    implementation("io.insert-koin:koin-annotations:1.0.0-beta-1") // https://medium.com/koin-developers/koin-annotations-c06c2b876ebe
    ksp("io.insert-koin:koin-ksp-compiler:1.0.0-beta-1")
    // Network
    implementation("com.squareup.retrofit2:retrofit:2.7.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.squareup.retrofit2:converter-moshi:2.7.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.moshi:moshi-adapters:1.8.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.8.0")
    // Google API
    implementation("com.google.maps.android:maps-ktx:3.2.1")
    implementation("com.google.android.gms:play-services-maps:18.0.2")

    //_________TEST Dependencies_____________________________________________
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.room:room-testing:2.4.2")
    testImplementation("android.arch.core:core-testing:1.1.1")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("io.insert-koin:koin-test:3.2.0-beta-1")
    testImplementation("io.insert-koin:koin-test-junit4:3.2.0-beta-1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
    testImplementation("org.robolectric:robolectric:4.3.1") {
        exclude(group = "com.google.auto.service", module = "auto-service")
    }
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

inline fun <reified ValueT> com.android.build.api.dsl.VariantDimension.buildConfigField(
    name: String,
    value: ValueT
) {
    val resolvedValue = when (value) {
        is String -> "\"$value\"" // hate this
        else -> value
    }.toString()
    buildConfigField(ValueT::class.java.simpleName, name, resolvedValue)
}