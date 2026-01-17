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
    navController: NavHostController = rememberNavController()
) {
    // REVISI: Ganti nama state agar jelas bahwa ini adalah NAMA LENGKAP dari database
    var userRole by remember { mutableStateOf("user") }
    var namaLengkapUser by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            HalamanSplash(
                onTimeout = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            HalamanLogin(
                onLoginSuccess = { role, nama ->
                    // REVISI: Simpan data nama (dari kolom nama di DB) ke state navigasi
                    userRole = role
                    namaLengkapUser = nama ?: "Kader IMM"

                    navController.navigate(DestinasiHome.route) {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavToRegister = { navController.navigate("register") }
            )
        }

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

        composable(route = DestinasiHome.route) {
            HalamanHome(
                role = userRole,
                // REVISI: Kirim namaLengkapUser ke Dashboard
                username = namaLengkapUser,
                onAddClick = { navController.navigate(DestinasiEntry.route) },
                onLogoutClick = {
                    // Reset data saat logout
                    userRole = "user"
                    namaLengkapUser = ""
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

        // --- Sisanya tetap sama ---
        composable("list_kader") {
            HalamanListKader(
                role = userRole,
                onDetailClick = { id -> navController.navigate("${DestinasiDetail.route}/$id") },
                onAddClick = { navController.navigate(DestinasiEntry.route) },
                navigateBack = { navController.popBackStack() },
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }

        composable("grafik_kader") {
            HalamanGrafikKader(
                navigateBack = { navController.popBackStack() },
                viewModel = viewModel(factory = PenyediaViewModel.Factory)
            )
        }

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

        composable(
            route = DestinasiDetail.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetail.kaderIdArg) { type = NavType.IntType })
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

        composable(
            route = DestinasiEdit.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEdit.kaderIdArg) { type = NavType.IntType })
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