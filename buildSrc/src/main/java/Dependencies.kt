/**
 * Updated by Baljeet singh.
 * **/

object Versions{

    const val core = "1.10.1"
    const val lifecycle = "2.6.1"
    const val activity_compose = "1.7.2"
    const val compose = "1.4.3"
    const val compose_navigation = "2.6.0"
    const val material_version = "1.2.0-alpha02"
    const val junit = "4.13.2"
    const val androidx_test_ext = "1.1.5"
    const val androidx_test_espresso = "3.5.1"
    const val androidx_material_icons = "1.4.3"
    const val kotlinx_datetime = "0.4.0"
    const val compose_splash_screen = "1.0.1"
    const val coil = "2.4.0"
    const val accompanist = "0.27.0"
    const val maxkeppeler = "1.0.2"
    const val dagger = "2.46.1"
    const val hilt = "1.0.0"
}

object Depends{


    //android core
    const val core = "androidx.core:core-ktx:${Versions.core}"
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycle_runtime_compose = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycle}"
    const val lifecycle_viewmodel_compose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}"


    //compose
    const val activity_compose = "androidx.activity:activity-compose:${Versions.activity_compose}"
    const val compose = "androidx.compose.ui:ui:${Versions.compose}"
    const val compose_ui_tooling_preview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
    const val compose_navigation = "androidx.navigation:navigation-compose:${Versions.compose_navigation}"
    const val compose_foundation = "androidx.compose.foundation:foundation:${Versions.compose}"
    const val compose_ui_test = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
    const val compose_ui_tooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"

    //material
    const val material = "androidx.compose.material3:material3:${Versions.material_version}"
    const val androidx_material_icons = "androidx.compose.material:material-icons-extended:${Versions.androidx_material_icons}"

    //test
    const val junit = "junit:junit:${Versions.junit}"
    const val androidx_test_ext = "androidx.test.ext:junit:${Versions.androidx_test_ext}"
    const val androidx_test_espresso = "androidx.test.espresso:espresso-core:${Versions.androidx_test_espresso}"

    //firebase
    const val firebase_bom = "com.google.firebase:firebase-bom:32.1.0"
    const val firebase_auth = "com.google.firebase:firebase-auth-ktx"
    const val firebase_firestore = "com.google.firebase:firebase-firestore-ktx"
    const val firebase_functions = "com.google.firebase:firebase-functions-ktx"

    //Kotlinx datetime
    const val kotlinx_datetime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinx_datetime}"

    //compose splash screen
    const val compose_splash_screen = "androidx.core:core-splashscreen:${Versions.compose_splash_screen}"

    //compose coil image loader
    const val coil =  "io.coil-kt:coil-compose:${Versions.coil}"

    //accompanist
    const val accompanist_system_ui_controller= "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}"
    const val accompanist_pager_indicator = "com.google.accompanist:accompanist-pager-indicators:${Versions.accompanist}"
    const val accompanist_pager = "com.google.accompanist:accompanist-pager:${Versions.accompanist}"

    //maxkeppeler sheets
    const val maxkeppeler_core = "com.maxkeppeler.sheets-compose-dialogs:core:${Versions.maxkeppeler}"
    const val maxkeppeler_calendar = "com.maxkeppeler.sheets-compose-dialogs:calendar:${Versions.maxkeppeler}"
    const val maxkeppeler_clock = "com.maxkeppeler.sheets-compose-dialogs:clock:${Versions.maxkeppeler}"

    //dagger hilt
    const val dagger_hilt = "com.google.dagger:hilt-android:${Versions.dagger}"
    const val dagger_compiler = "com.google.dagger:hilt-android-compiler:${Versions.dagger}"
    const val hilt_compiler ="androidx.hilt:hilt-compiler:${Versions.hilt}"
    const val hilt_navigation_compose = "androidx.hilt:hilt-navigation-compose:${Versions.hilt}"
}