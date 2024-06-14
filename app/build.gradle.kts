/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
  id("com.alipay.apollo.baseline.config")
}

android {
  compileSdk = libs.versions.compileSdk.get().toInt()
  buildFeatures {
    dataBinding = true
  }
  defaultConfig {
    applicationId = "com.google.samples.apps.sunflower"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    testInstrumentationRunner = "com.google.samples.apps.sunflower.utilities.MainTestRunner"
    versionCode = 1
    versionName = "1.0.0"
    vectorDrawables.useSupportLibrary = true

    // Consult the README on instructions for setting up Unsplash API key
    buildConfigField("String", "UNSPLASH_ACCESS_KEY", "\"" + getUnsplashAccess() + "\"")
    javaCompileOptions {
      annotationProcessorOptions {
        arguments["dagger.hilt.disableModulesHaveInstallInCheck"] = "true"
      }
    }
  }
  signingConfigs {
//    // Define a new signing config for debug
//    create("debug") {
//      storeFile = file("path/to/your/debug.keystore")
//      storePassword = "yourStorePassword"
//      keyAlias = "yourKeyAlias"
//      keyPassword = "yourKeyPassword"
//    }
    // Instead of creating a new 'debug', get the reference to the existing one
    getByName("debug") {
      // Modify the existing debug signing config with your custom details
//      storeFile = file("keystore/sunflower.keystore")
      storeFile = file("keystore/sunflower.keystore")
      storePassword = "123456"
      keyAlias = "sunflower"
      keyPassword = "123456"

      enableV1Signing = true
      enableV2Signing = true
      enableV3Signing = true
      enableV4Signing = true

//      // 添加这一行来启用v1签名
//      v1SigningEnabled = true
    }
  }
  buildTypes {
    // No need to explicitly set the signingConfig for 'debug' here;
    // it will automatically use the modified 'debug' signing config.
    getByName("debug") {
      // Apply the custom debug signing config
      signingConfig = signingConfigs.getByName("debug")
//      // 在对应的buildType中直接启用v1签名
//      v1SigningEnabled = true
//      v2SigningEnabled = true // 确保v2签名也启用，保持最佳兼容性
      // 假设您的签名配置已正确设置，这里仅展示如何在buildType中控制
      // 注意：直接控制v1/v2的属性名称可能会有所不同，以下为示意
//      signingConfig {
//        enableV1Signing = true
//        enableV2Signing = true
//      }
    }
    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }
    create("benchmark") {
      initWith(getByName("release"))
      signingConfig = signingConfigs.getByName("debug")
      isDebuggable = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules-benchmark.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    // work-runtime-ktx 2.1.0 and above now requires Java 8
    jvmTarget = JavaVersion.VERSION_17.toString()

    // Enable Coroutines and Flow APIs
    freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.FlowPreview"
  }
  buildFeatures {
    compose = true
    dataBinding = true
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }
  packagingOptions {
    // Multiple dependency bring these files in. Exclude them to enable
    // our test APK to build (has no effect on our AARs)
    resources.excludes += "/META-INF/AL2.0"
    resources.excludes += "/META-INF/LGPL2.1"
  }

  testOptions {
    managedDevices {
      devices {
        maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("pixel2api27").apply {
          device = "Pixel 2"
          apiLevel = 27
          systemImageSource = "aosp"
        }
      }
    }
  }
  namespace = "com.google.samples.apps.sunflower"
}

androidComponents {
  onVariants(selector().withBuildType("release")) {
    // Only exclude *.version files in release mode as debug mode requires
    // these files for layout inspector to work.
    it.packaging.resources.excludes.add("META-INF/*.version")
  }
}

dependencies {
  implementation(platform("com.mpaas.android:${rootProject.ext["mpaas_artifact"]}:${rootProject.ext["mpaas_baseline"]}"))
  //移动网关
  implementation(libs.rpc)
  implementation(libs.android.blueshield)
  implementation(libs.tinyapp)
  implementation(libs.uccore)
  implementation(libs.apm)
  implementation(project(":codescanner"))
  ksp(libs.androidx.room.compiler)
  ksp(libs.hilt.android.compiler)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.livedata.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.paging.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.work.runtime.ktx)
  implementation(libs.material)
  implementation(libs.gson)
  implementation(libs.okhttp3.logging.interceptor)
  implementation(libs.retrofit2.converter.gson)
  implementation(libs.retrofit2)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.hilt.android)
  implementation(libs.hilt.navigation.compose)
  implementation(libs.androidx.profileinstaller)
  implementation(libs.blueshield)//蓝盾SDK依赖
  implementation("com.mpaas.android:logging")//日志组件SDK依赖，如果不添加，会导致不打印可信模块管理器构造实例成功


  // Compose
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.constraintlayout.compose)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.foundation.layout)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui.viewbinding)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.runtime.livedata)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.glide)
  implementation(libs.accompanist.systemuicontroller)
  debugImplementation(libs.androidx.compose.ui.tooling)

  // Testing dependencies
  debugImplementation(libs.androidx.monitor)
  kspAndroidTest(libs.hilt.android.compiler)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.arch.core.testing)
  androidTestImplementation(libs.androidx.espresso.contrib)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.espresso.intents)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.test.uiautomator)
  androidTestImplementation(libs.androidx.work.testing)
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.guava)
  androidTestImplementation(libs.hilt.android.testing)
  androidTestImplementation(libs.accessibility.test.framework)
  androidTestImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.junit)
}

fun getUnsplashAccess(): String? {
  return project.findProperty("unsplash_access_key") as? String
}

// 使用 exclude 排除特定依赖
configurations.all {
  resolutionStrategy {
    exclude(group = "com.alipay.android.phone.thirdparty", module = "securityguard-build")
  }
}