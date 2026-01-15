package com.example.simkader_236.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simkader_236.viewmodel.EditViewModel
import com.example.simkader_236.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEdit(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val warnaUtama = Color(0xFFB71C1C)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Data Kader", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
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
                .background(Color(0xFFFFEBEE))
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Pembaruan Data Personal", fontWeight = FontWeight.Bold, color = warnaUtama, modifier = Modifier.padding(bottom = 16.dp))

                    // Input Nama
                    OutlinedTextField(
                        value = viewModel.kaderUiState.detailKader.nama,
                        onValueChange = { viewModel.updateUiState(viewModel.kaderUiState.detailKader.copy(nama = it)) },
                        label = { Text("Nama Lengkap") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = warnaUtama) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Input NIM (Read Only karena biasanya NIM adalah primary key/ID unik)
                    OutlinedTextField(
                        value = viewModel.kaderUiState.detailKader.nim,
                        onValueChange = {},
                        label = { Text("NIM (Tidak dapat diubah)") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFF5F5F5), unfocusedContainerColor = Color(0xFFF5F5F5))
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Data Akademik & Kaderisasi", fontWeight = FontWeight.Bold, color = warnaUtama, modifier = Modifier.padding(bottom = 16.dp))

                    // Dropdown Program Studi
                    CustomDropdownMenu(
                        label = "Program Studi",
                        options = listOf("Teknologi Informasi", "Teknik Mesin", "Teknik Sipil", "Teknik Elektro", "Teknologi Elektro Medis", "Teknologi Rekayasa Otomotif"),
                        selectedOption = viewModel.kaderUiState.detailKader.prodi,
                        onOptionSelected = { viewModel.updateUiState(viewModel.kaderUiState.detailKader.copy(prodi = it)) },
                        warnaUtama = warnaUtama
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dropdown Angkatan
                    CustomDropdownMenu(
                        label = "Angkatan",
                        options = listOf("2021", "2022", "2023", "2024", "2025"),
                        selectedOption = viewModel.kaderUiState.detailKader.angkatan,
                        onOptionSelected = { viewModel.updateUiState(viewModel.kaderUiState.detailKader.copy(angkatan = it)) },
                        warnaUtama = warnaUtama
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dropdown Status Perkaderan
                    CustomDropdownMenu(
                        label = "Status Kaderisasi",
                        options = listOf("Darul Arqam Dasar (DAD)", "Darul Arqam Madya (DAM)", "Darul Arqam Paripurna (DAP)"),
                        selectedOption = viewModel.kaderUiState.detailKader.status,
                        onOptionSelected = { viewModel.updateUiState(viewModel.kaderUiState.detailKader.copy(status = it)) },
                        warnaUtama = warnaUtama
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Tombol Update
                    Button(
                        onClick = { viewModel.updateKader(navigateBack) },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = warnaUtama),
                        enabled = viewModel.kaderUiState.isEntryValid
                    ) {
                        Icon(Icons.Default.Update, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("PERBARUI DATA KADER", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}