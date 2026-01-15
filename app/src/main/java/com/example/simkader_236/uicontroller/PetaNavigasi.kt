package com.example.simkader_236.uicontroller

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simkader_236.uicontroller.route.*

@Composable
fun SIMKaderApp(navController: NavHostController = rememberNavController()) {
    PetaNavigasi(navController = navController)
}

@Composable
fun PetaNavigasi(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route, // Mulai dari Dashboard sesuai flowchart [cite: 66, 388]
        modifier = modifier
    ) {
        // Halaman Home/Dashboard
        composable(DestinasiHome.route) {
            // Nantinya memanggil HomeScreen [cite: 501]
        }

        // Halaman Tambah Kader
        composable(DestinasiEntry.route) {
            // Nantinya memanggil EntryKaderScreen [cite: 506]
        }

        // Halaman Detail Kader
        composable(
            route = DestinasiDetail.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetail.kaderIdArg) {
                type = NavType.IntType
            })
        ) {
            // Nantinya memanggil DetailKaderScreen [cite: 511]
        }

        // Halaman Edit Kader
        composable(
            route = DestinasiEdit.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEdit.kaderIdArg) {
                type = NavType.IntType
            })
        ) {
            // Nantinya memanggil EditKaderScreen [cite: 515]
        }
    }
}