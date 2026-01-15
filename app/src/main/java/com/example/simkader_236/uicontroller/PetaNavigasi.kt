package com.example.simkader_236.uicontroller

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simkader_236.uicontroller.route.*
import com.example.simkader_236.view.*
import com.example.simkader_236.viewmodel.*
import com.example.simkader_236.viewmodel.provider.PenyediaViewModel

@Composable
fun SIMKaderApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()) {
    PetaNavigasi(navController = navController)
}

@Composable
fun PetaNavigasi(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()) {

    // Simpan state user agar bisa digunakan di berbagai halaman
    var userRole by remember { mutableStateOf("user") }
    var userName by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // 1. SPLASH SCREEN
        composable("splash") {
            HalamanSplash(
                onTimeout = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // 2. LOGIN
        composable("login") {
            HalamanLogin(
                onLoginSuccess = { role, name -> // Sesuaikan parameter untuk menerima nama
                    userRole = role
                    userName = name ?: "Kader IMM" // Jika nama kosong, gunakan default
                    navController.navigate(DestinasiHome.route) {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavToRegister = { navController.navigate("register") }
            )
        }

        // 3. REGISTER
        composable("register") {
            HalamanRegister(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                navigateBack = { navController.popBackStack() }
            )
        }

        // 4. DASHBOARD UTAMA (REVISI: Menambah Parameter username)
        composable(route = DestinasiHome.route) {
            HalamanHome(
                role = userRole,
                username = userName, // KIRIM DATA USERNAME DISINI
                onAddClick = { navController.navigate(DestinasiEntry.route) },
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo(DestinasiHome.route) { inclusive = true }
                    }
                },
                onNavToList = { navController.navigate("list_kader") },
                onNavToGrafik = { navController.navigate("grafik_kader") },
                modifier = modifier,
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }

        // 5. HALAMAN LIST KADER
        composable("list_kader") {
            HalamanListKader(
                role = userRole,
                onDetailClick = { id ->
                    navController.navigate("${DestinasiDetail.route}/$id")
                },
                onAddClick = {
                    navController.navigate(DestinasiEntry.route)
                },
                navigateBack = { navController.popBackStack() },
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }

        // 6. HALAMAN GRAFIK KADER
        composable("grafik_kader") {
            HalamanGrafikKader(
                navigateBack = { navController.popBackStack() },
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }

        // 7. ENTRY DATA (ADMIN ONLY)
        composable(DestinasiEntry.route) {
            if (userRole == "admin") {
                HalamanEntry(
                    viewModel = viewModel(factory = PenyediaViewModel.Factory),
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            } else {
                navController.popBackStack()
            }
        }

        // 8. DETAIL DATA
        composable(
            route = DestinasiDetail.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetail.kaderIdArg) {
                type = NavType.IntType
            })
        ) {
            HalamanDetail(
                role = userRole,
                viewModel = viewModel(factory = PenyediaViewModel.Factory),
                navigateBack = { navController.popBackStack() },
                onEditClick = { id ->
                    if (userRole == "admin") {
                        navController.navigate("${DestinasiEdit.route}/$id")
                    }
                }
            )
        }

        // 9. EDIT DATA (ADMIN ONLY)
        composable(
            route = DestinasiEdit.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEdit.kaderIdArg) {
                type = NavType.IntType
            })
        ) {
            if (userRole == "admin") {
                HalamanEdit(
                    viewModel = viewModel(factory = PenyediaViewModel.Factory),
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}