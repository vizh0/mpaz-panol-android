package com.example.mpaz_panol.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mpaz_panol.ui.auth.AuthViewModel
import com.example.mpaz_panol.ui.auth.LoginScreen
import com.example.mpaz_panol.ui.inventory.CatalogScreen
import com.example.mpaz_panol.ui.inventory.InventoryViewModel
import com.example.mpaz_panol.ui.main.HomeScreen
import com.example.mpaz_panol.ui.request.HistoryScreen
import com.example.mpaz_panol.ui.request.RequestScreen
import com.example.mpaz_panol.ui.request.RequestViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    inventoryViewModel: InventoryViewModel,
    requestViewModel: RequestViewModel
) {
    val navController = rememberNavController()
    val currentUser by authViewModel.currentUser.collectAsState()
    val isSessionChecked by authViewModel.isSessionChecked.collectAsState()

    if (!isSessionChecked) return

    val startDestination = if (currentUser != null) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            currentUser?.let { user ->
                HomeScreen(
                    user = user,
                    onNavigateToCatalog = { navController.navigate("catalog") },
                    onNavigateToRequest = { navController.navigate("request") },
                    onNavigateToHistory = { navController.navigate("history") },
                    onLogout = {
                        authViewModel.logout {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
        composable("catalog") {
            currentUser?.let { user ->
                CatalogScreen(
                    user = user,
                    viewModel = inventoryViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onNavigateToRequest = { navController.navigate("request") },
                    onNavigateToHistory = { navController.navigate("history") }
                )
            }
        }
        composable("request") {
            currentUser?.let { user ->
                RequestScreen(
                    user = user,
                    inventoryViewModel = inventoryViewModel,
                    requestViewModel = requestViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onNavigateToCatalog = { navController.navigate("catalog") },
                    onNavigateToHistory = { navController.navigate("history") },
                    onRequestSubmitted = {
                        navController.navigate("history") {
                            popUpTo("home")
                        }
                    }
                )
            }
        }
        composable("history") {
            currentUser?.let { user ->
                HistoryScreen(
                    user = user,
                    requestViewModel = requestViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onNavigateToCatalog = { navController.navigate("catalog") },
                    onNavigateToRequest = { navController.navigate("request") }
                )
            }
        }
    }
}