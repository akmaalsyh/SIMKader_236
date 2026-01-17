package com.example.simkader_236.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simkader_236.viewmodel.DetailUiState
import com.example.simkader_236.viewmodel.DetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDetail(
    role: String,
    viewModel: DetailViewModel,
    navigateBack: () -> Unit,
    onEditClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Memaksa pengambilan data terbaru setiap kali halaman profil dibuka
    LaunchedEffect(Unit) {
        viewModel.getKaderById()
    }

    val uiState = viewModel.detailUiState
    val warnaUtama = Color(0xFFB71C1C)
    val scope = rememberCoroutineScope()

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showDeleteSuccessDialog by remember { mutableStateOf(false) }

    // Dialog Konfirmasi Hapus (Gaya Logout)
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Konfirmasi Hapus", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin menghapus data kader ini secara permanen dari sistem?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        viewModel.deleteKader(
                            onSuccess = { showDeleteSuccessDialog = true },
                            onShowMessage = { /* Handle error jika perlu */ }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = warnaUtama)
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }

    // Dialog Berhasil Hapus (Gaya Pop-up Sukses)
    if (showDeleteSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Hapus Berhasil", fontWeight = FontWeight.Bold) },
            text = { Text("Data kader telah berhasil dihapus dari sistem SIM-KADER.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteSuccessDialog = false
                        navigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = warnaUtama)
                ) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profil Kader", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = warnaUtama)
            )
        },
        floatingActionButton = {
            if (role == "admin" && uiState is DetailUiState.Success) {
                FloatingActionButton(
                    onClick = { onEditClick(uiState.kader.id_kader.toInt()) },
                    containerColor = warnaUtama,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFFFEBEE))
                .verticalScroll(rememberScrollState())
        ) {
            when (uiState) {
                is DetailUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize().padding(top = 50.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = warnaUtama)
                    }
                }
                is DetailUiState.Success -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(180.dp)
                            .background(Brush.verticalGradient(listOf(warnaUtama, Color(0xFFD32F2F)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.White)
                            Text(uiState.kader.nama.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }

                    Card(
                        modifier = Modifier.padding(16.dp).offset(y = (-30).dp).fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(all = 20.dp)) {
                            DetailItemRow(label = "NIM", value = uiState.kader.nim, icon = Icons.Default.Badge)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                            DetailItemRow(label = "Program Studi", value = uiState.kader.prodi, icon = Icons.Default.School)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                            // REVISI: Sekarang bisa menerima Int dari database tanpa error
                            DetailItemRow(label = "Angkatan", value = uiState.kader.angkatan, icon = Icons.Default.DateRange)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                            DetailItemRow(label = "Status Perkaderan", value = uiState.kader.status, icon = Icons.Default.Stars)
                        }
                    }

                    if (role == "admin") {
                        OutlinedButton(
                            onClick = { showDeleteConfirmDialog = true },
                            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = warnaUtama),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Hapus Data Kader")
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
                is DetailUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Gagal memuat profil kader", color = Color.Gray)
                    }
                }
            }
        }
    }
}

// REVISI: Mengubah tipe parameter 'value' menjadi 'Any' agar mendukung Int dan String
@Composable
fun DetailItemRow(label: String, value: Any, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFB71C1C), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            // .toString() di sini memastikan data apa pun bisa ditampilkan sebagai teks
            Text(text = value.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}