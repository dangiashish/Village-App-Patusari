plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("D:\\AppsKeyStore\\Patusari\\patusari-keystore.jks")
            storePassword = "ashish8939"
            keyAlias = "key0"
            keyPassword = "ashish8939"
        }
    }
    namespace = "com.jhunjhunu.patusari"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.jhunjhunu.patusari"
        minSdk = 24
        targetSdk = 33
        versionCode = 107
        versionName = "1.0.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("release")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("com.getbase:floatingactionbutton:1.10.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("de.cketti.library.changelog:ckchangelog:1.2.2")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.orhanobut:dialogplus:1.11@aar")
    implementation("com.airbnb.android:lottie:6.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.github.skydoves:balloon:1.4.7")
    implementation("com.google.firebase:firebase-auth:22.1.2")
    implementation("com.google.firebase:firebase-database:20.2.2")
    implementation("com.firebaseui:firebase-ui-database:8.0.0")
    implementation("com.google.firebase:firebase-messaging:23.2.1")
    implementation("com.google.firebase:firebase-analytics:21.3.0")
    implementation("com.google.firebase:firebase-config:21.4.1")
    implementation("com.google.firebase:firebase-storage:20.2.1")
//    implementation("")
//    implementation("")
//    implementation("")
//    implementation("")
//    implementation("")
//    implementation("")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}