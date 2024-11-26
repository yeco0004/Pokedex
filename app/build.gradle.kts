plugins {
    alias(libs.plugins.android.application) //aplicaciones android
    alias(libs.plugins.kotlin.android)//soporte de Kotlin en android
}

android {
    namespace = "com.cursoandroid.pokedex" //Espacio de nombre para la app
    compileSdk = 35//SDK de compilacion utilizado

    defaultConfig {
        applicationId = "com.cursoandroid.pokedex" //Id de la app
        minSdk = 21 //Version minima de SDK
        targetSdk = 34 //Version de SDK objetivo para la app
        versionCode = 1 //còdigo de version de app
        versionName = "1.0" // Nombre de version de app

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" //Runner para pruebas
        vectorDrawables {
            useSupportLibrary = true // Habilita el uso de la biblioteca de soporte para gráficos vectoriales
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false // No se habilita la ofuscación de código en la versión de lanzamien
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 //compatibilidad de fuente con java 8
        targetCompatibility = JavaVersion.VERSION_1_8 //compatibilidad de destino con java 8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val room_version = "2.6.1" //Definicion de la version Room
    implementation(libs.androidx.core.ktx) // Extensiones de Kotlin para la biblioteca Core de AndroidX
    implementation(libs.androidx.lifecycle.runtime.ktx) // Extensiones de Kotlin para el ciclo de vida de AndroidX
    implementation(libs.androidx.activity.compose) // Soporte para actividades en Jetpack Compose
    implementation(platform(libs.androidx.compose.bom)) // BOM para gestionar versiones de Compose
    implementation(libs.androidx.ui) // Biblioteca de UI de AndroidX
    implementation(libs.androidx.ui.graphics) // Biblioteca para gráficos en UI de AndroidX
    implementation(libs.androidx.ui.tooling.preview) // Herramientas de vista previa para UI de AndroidX
    implementation(libs.androidx.material3) // Biblioteca Material Design 3 para componentes de UI

    // Dependencias para pruebas
    testImplementation(libs.junit) // Framework de pruebas JUnit para pruebas unitarias
    androidTestImplementation(libs.androidx.junit) // JUnit para pruebas de Android
    androidTestImplementation(libs.androidx.espresso.core) // Espresso para pruebas de UI en Android
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM para pruebas de Compose
    androidTestImplementation(libs.androidx.ui.test.junit4) // Soporte para pruebas de UI con JUnit 4
    debugImplementation(libs.androidx.ui.tooling) // Herramientas de depuración para UI de AndroidX
    debugImplementation(libs.androidx.ui.test.manifest) // Manifesto de pruebas para UI en modo de depuración


    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:2.11.0")

    // Dependencias para Room
    implementation("androidx.room:room-runtime:$room_version") // Runtime de Room para la base de datos
    annotationProcessor("androidx.room:room-compiler:$room_version") // Procesador de anotaciones para Room
    implementation("androidx.room:room-ktx:$room_version") // Extensiones de Kotlin para Room
    implementation("androidx.room:room-rxjava2:$room_version") // Soporte

}
