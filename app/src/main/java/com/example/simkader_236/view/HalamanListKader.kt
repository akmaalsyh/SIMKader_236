package com.example.simkader_236.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simkader_236.modeldata.DataKader
import com.example.simkader_236.viewmodel.HomeUiState
import com.example.simkader_236.viewmodel.ListKaderViewModel
import com.example.simkader_236.viewmodel.provider.PenyediaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanListKader(
    role: String,
    onDetailClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    navigateBack: () -> Unit,
    viewModel: ListKaderViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState = viewModel.listUiState
    val warnaUtama = Color(0xFFB71C1C) // Merah Maroon IMM

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getAllKader()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Daftar Seluruh Kader", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = warnaUtama)
            )
        },
        // --- LOGIKA FLOATING ACTION BUTTON (FAB) ---
        floatingActionButton = {
            // Tombol tambah hanya muncul jika login sebagai admin
            if (role == "admin") {
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = warnaUtama,
                    contentColor = Color.White,
                    shape = CircleShape // Menggunakan bentuk bulat agar lebih standar
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Kader")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // Search Bar Card
            Card(
                modifier = Modifier.padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Cari Nama atau NIM...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = warnaUtama) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    scope.launch {
                        isRefreshing = true
                        viewModel.getAllKader()
                        delay(1000)
                        isRefreshing = false
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (uiState) {
                        is HomeUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = warnaUtama)
                        }
                        is HomeUiState.Success -> {
                            val filteredList = uiState.kader.filter {
                                it.nama.contains(searchQuery, ignoreCase = true) || it.nim.contains(searchQuery)
                            }

                            if (filteredList.isEmpty()) {
                                Text("Data tidak ditemukan", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                            } else {
                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                    contentPadding = PaddingValues(bottom = 80.dp) // Jarak agar tidak tertutup FAB
                                ) {
                                    items(filteredList) { kader ->
                                        CardKader(
                                            kader = kader,
                                            onItemClick = { onDetailClick(kader.id_kader.toInt()) }
                                        )
                                    }
                                }
                            }
                        }
                        is HomeUiState.Error -> Text("Gagal memuat data", modifier = Modifier.align(Alignment.Center))
                    }

                    // Quick Scroll Up Button (Opsional jika FAB tambah sudah ada)
                    val showButton by remember { derivedStateOf { listState.firstVisibleItemIndex > 1 } }
                    if (showButton) {
                        SmallFloatingActionButton(
                            onClick = { scope.launch { listState.animateScrollToItem(0) } },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 16.dp, bottom = 80.dp), // Ditaruh di atas FAB utama
                            containerColor = Color.Gray.copy(alpha = 0.6f),
                            contentColor = Color.White
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardKader(kader: DataKader, onItemClick: () -> Unit) {
    Card(
        onClick = onItemClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(45.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFFEBEE)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = kader.nama.take(1).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB71C1C),
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = kader.nama,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "NIM: ${kader.nim}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    color = Color(0xFFB71C1C),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = kader.prodi,
                        color = Color.White,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFFB71C1C)
                )
                Text(
                    text = "Detail",
                    fontSize = 10.sp,
                    color = Color(0xFFB71C1C),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}