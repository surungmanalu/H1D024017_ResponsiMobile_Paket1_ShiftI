package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.detail.DetailScreen
import com.example.myapplication.ui.screens.home.HomeScreen
import com.example.myapplication.ui.screens.home.HomeViewModel

@Composable
fun BmkgNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // 1. Home Screen (Katalog & Pencarian Wilayah Gempa)
        composable(route = Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToDetail = { gempaIndex ->
                    navController.navigate(Screen.Detail.createRoute(gempaIndex))
                }
            )
        }

        // 2. Detail Screen (Informasi Lengkap Seismik & Tombol Back)
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("gempaIndex") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val gempaIndex = backStackEntry.arguments?.getInt("gempaIndex") ?: 0
            val gempa = homeViewModel.getGempaByIndex(gempaIndex)
            DetailScreen(
                gempa = gempa,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
