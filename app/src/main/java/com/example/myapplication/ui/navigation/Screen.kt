package com.example.myapplication.ui.navigation

/**
 * Sealed class untuk rute navigasi aplikasi.
 * Membatasi navigasi tepat pada 2 layar sesuai spesifikasi (Home & Detail).
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Detail : Screen("detail/{gempaIndex}") {
        fun createRoute(index: Int): String = "detail/$index"
    }
}
