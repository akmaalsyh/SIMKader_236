package com.example.simkader_236.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simkader_236.R
import com.example.simkader_236.viewmodel.HomeUiState
import com.example.simkader_236.viewmodel.HomeViewModel
import com.example.simkader_236.viewmodel.provider.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    role: String,
    username: String, // Variabel ini sekarang berisi Nama Lengkap dari PetaNavigasi
    onAddClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onNavToList: () -> Unit,
    onNavToGrafik: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.getStatistik()
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uiState = viewModel.homeUiState
    val warnaMerahTua = Color(0xFFB71C1C)
    val warnaMerahMuda = Color(0xFFFFEBEE)

    val scrollState = rememberScrollState()
    val isScrolled by remember { derivedStateOf { scrollState.value > 100 } }

    var showLogoutDialog by remember { mutableStateOf(false) }

    // ALERT DIALOG LOGOUT
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Konfirmasi Keluar", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin keluar dari sistem SIM-KADER?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = warnaMerahTua)
                ) {
                    Text("Keluar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // 1. HEADER DRAWER (Profil User)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(warnaMerahTua)
                        .padding(24.dp)
                ) {
                    Column {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        // Menampilkan Nama Lengkap Kader
                        Text(text = username, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = role, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2. MENU NAVIGASI UTAMA
                NavigationDrawerItem(
                    label = { Text("List Kader") },
                    selected = false,
                    icon = { Icon(Icons.Default.People, contentDescription = null) },
                    onClick = { scope.launch { drawerState.close() }; onNavToList() }
                )
                NavigationDrawerItem(
                    label = { Text("Grafik Statistik") },
                    selected = false,
                    icon = { Icon(Icons.Default.PieChart, contentDescription = null) },
                    onClick = { scope.launch { drawerState.close() }; onNavToGrafik() }
                )

                // --- REVISI: DORONG LOGOUT KE BAWAH ---
                // Spacer dengan weight(1f) akan mengambil semua sisa ruang kosong
                Spacer(modifier = Modifier.weight(1f))

                HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))

                // 3. MENU LOGOUT DI PALING BAWAH
                NavigationDrawerItem(
                    label = { Text("Logout", color = Color.Red, fontWeight = FontWeight.Bold) },
                    selected = false,
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.Red) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        showLogoutDialog = true
                    },
                    modifier = Modifier.padding(bottom = 16.dp) // Memberi jarak dari tepi bawah layar
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("SIM-KADER IMM FT", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { showLogoutDialog = true }) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = warnaMerahTua)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            if (isScrolled) scrollState.animateScrollTo(0)
                            else scrollState.animateScrollTo(scrollState.maxValue)
                        }
                    },
                    containerColor = warnaMerahTua,
                    contentColor = Color.White,
                    shape = androidx.compose.foundation.shape.CircleShape
                ) {
                    Icon(
                        imageVector = if (isScrolled) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Scroll"
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // --- HERO SECTION ---
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.gambar_imm),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Selamat Datang,",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )
                            // MENAMPILKAN NAMA LENGKAP DENGAN HURUF KAPITAL
                            Text(
                                text = username.uppercase(),
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tentang SIM-KADER IMM FT",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = warnaMerahTua
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "SIM-KADER merupakan platform integrasi data bagi Ikatan Mahasiswa Muhammadiyah (IMM) Fakultas Teknik UMY. Aplikasi ini dirancang untuk mengelola data perkaderan secara digital demi mewujudkan tata kelola organisasi yang modern, transparan, dan berkelanjutan bagi seluruh kader cendekiawan berpribadi.",
                        fontSize = 13.sp,
                        textAlign = TextAlign.Justify,
                        lineHeight = 20.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Layanan Utama", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Kelola dan pantau data perkaderan secara digital", fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DashboardButton(
                            title = "List Kader",
                            desc = "Lihat & kelola data",
                            icon = Icons.Default.People,
                            onClick = onNavToList,
                            containerColor = warnaMerahTua,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardButton(
                            title = "Grafik",
                            desc = "Statistik persebaran",
                            icon = Icons.Default.PieChart,
                            onClick = onNavToGrafik,
                            containerColor = warnaMerahTua,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Data Statistik", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = warnaMerahMuda),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Ringkasan Data", color = warnaMerahTua, fontSize = 12.sp)
                                Text("Total Kader Terdata", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            if (uiState is HomeUiState.Success) {
                                Text(
                                    text = "${uiState.kader.size}",
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Black,
                                    color = warnaMerahTua
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(64.dp))

                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "© 2026 IMM FT UMY. All Rights Reserved.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Sistem Informasi Manajemen Kader",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 9.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

// Komponen tombol tetap sama
@Composable
fun DashboardButton(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    containerColor: Color,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(desc, fontSize = 10.sp, fontWeight = FontWeight.Normal, textAlign = TextAlign.Center)
        }
    }
}