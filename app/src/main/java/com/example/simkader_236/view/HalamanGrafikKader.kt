package com.example.simkader_236.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simkader_236.modeldata.DataKader
import com.example.simkader_236.viewmodel.HomeUiState
import com.example.simkader_236.viewmodel.HomeViewModel
import com.example.simkader_236.viewmodel.GrafikKaderViewModel
import com.example.simkader_236.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanGrafikKader(
    navigateBack: () -> Unit,
    viewModel: GrafikKaderViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.grafikUiState
    val warnaUtama = Color(0xFFB71C1C) // Merah Maroon IMM

    // Perbaikan 1: Fungsi pemanggil data
    LaunchedEffect(Unit) {
        viewModel.getGrafikData() // Ubah dari getKader() menjadi getGrafikData()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Statistik & Grafik", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = warnaUtama)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is HomeUiState.Loading -> CircularProgressIndicator(color = warnaUtama)
                // Perbaikan 2: Masalah type mismatch pada StatistikCard
                // Pastikan groupingBy menggunakan nilai String agar Map yang dihasilkan adalah Map<String, Int>
                is HomeUiState.Success -> {
                    StatistikCard(
                        title = "Persebaran Program Studi",
                        data = uiState.kader.groupingBy { it.prodi }.eachCount(),
                        warnaBar = warnaUtama
                    )

                    StatistikCard(
                        title = "Statistik Angkatan",
                        // Gunakan it.angkatan.toString() jika angkatan di model adalah Int
                        data = uiState.kader.groupingBy { it.angkatan.toString() }.eachCount(),
                        warnaBar = Color(0xFF1976D2)
                    )

                    StatistikCard(
                        title = "Status Perkaderan",
                        data = uiState.kader.groupingBy { it.status }.eachCount(),
                        warnaBar = Color(0xFF388E3C)
                    )
                }
                is HomeUiState.Error -> Text("Gagal memuat grafik.")
            }
        }
    }
}

@Composable
fun StatistikCard(title: String, data: Map<String, Int>, warnaBar: Color) {
    val maxCount = data.values.maxOrNull() ?: 1

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            data.forEach { (label, count) ->
                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = label, style = MaterialTheme.typography.bodySmall)
                        Text(text = "$count Orang", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    // Bar Chart Horizontal
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(count.toFloat() / maxCount)
                                .height(12.dp)
                                .background(warnaBar, RoundedCornerShape(6.dp))
                        )
                    }
                }
            }
        }
    }
}