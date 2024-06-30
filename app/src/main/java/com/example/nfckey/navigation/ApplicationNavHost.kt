package com.example.nfckey.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nfckey.ui.screens.home.HomeScreen
import com.example.nfckey.ui.screens.read.ReadPage

@Composable
fun ApplicationNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = AppRoutes.Home.route,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current){
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppRoutes.Home.route) {
            HomeScreen(hiltViewModel(viewModelStoreOwner), navController)
        }

        composable(AppRoutes.Scan.route){
            ReadPage(hiltViewModel(viewModelStoreOwner), navController)
        }
    }
}