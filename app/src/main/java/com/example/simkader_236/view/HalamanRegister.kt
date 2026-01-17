package com.example.simkader_236.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simkader_236.R
import com.example.simkader_236.viewmodel.RegisterViewModel
import com.example.simkader_236.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanRegister(
    onRegisterSuccess: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val warnaUtama = Color(0xFFB71C1C)

    // 1. POP-UP BERHASIL DAFTAR (Konsisten dengan Foto 2)
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Pendaftaran Berhasil", fontWeight = FontWeight.Bold) },
            text = { Text("Akun Anda telah berhasil dibuat. Silakan gunakan username '${viewModel.username.lowercase()}' untuk masuk ke sistem.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onRegisterSuccess() // Navigasi ke halaman Login
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = warnaUtama)
                ) {
                    Text("OK, Login Sekarang", color = Color.White)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFEBEE)) // Background senada dengan halaman lain
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_imm),
            contentDescription = "Logo IMM",
            modifier = Modifier.size(130.dp).padding(bottom = 16.dp)
        )

        Text(text = "DAFTAR AKUN", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = warnaUtama)
        Text(
            text = "Gabung ke Sistem Informasi Kader Teknik",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                // Nama Lengkap (Sekarang divalidasi tidak boleh ganda oleh PHP)
                OutlinedTextField(
                    value = viewModel.namaLengkap,
                    onValueChange = { viewModel.namaLengkap = it },
                    label = { Text("Nama Lengkap") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = warnaUtama) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Username
                OutlinedTextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.username = it.trim() },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null, tint = warnaUtama) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = warnaUtama) },
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, contentDescription = null, tint = warnaUtama)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Konfirmasi Password
                OutlinedTextField(
                    value = viewModel.confirmPassword,
                    onValueChange = { viewModel.confirmPassword = it },
                    label = { Text("Konfirmasi Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = warnaUtama) },
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(image, contentDescription = null, tint = warnaUtama)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )

                // PESAN ERROR (Akan menampilkan "Nama sudah terdaftar" dari PHP)
                if (viewModel.registerError != null) {
                    Text(
                        text = viewModel.registerError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Daftar
                Button(
                    onClick = {
                        viewModel.register(onSuccess = { showSuccessDialog = true })
                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = warnaUtama),
                    enabled = !viewModel.isLoading
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("DAFTAR SEKARANG", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }

        Row(modifier = Modifier.padding(top = 24.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Sudah punya akun?", color = Color.Gray)
            TextButton(onClick = navigateBack) {
                Text("Login di sini", color = warnaUtama, fontWeight = FontWeight.Bold)
            }
        }
    }
}