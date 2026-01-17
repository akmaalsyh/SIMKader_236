package com.example.simkader_236.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import com.example.simkader_236.viewmodel.LoginViewModel
import com.example.simkader_236.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanLogin(
    // REVISI: Tambahkan String kedua untuk Username
    onLoginSuccess: (String, String) -> Unit,
    onNavToRegister: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val warnaUtama = Color(0xFFB71C1C) // Merah Maroon IMM

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Logo IMM FT UMY
        Image(
            painter = painterResource(id = R.drawable.logo_imm),
            contentDescription = "Logo IMM",
            modifier = Modifier
                .size(130.dp)
                .padding(bottom = 16.dp)
        )

        // 2. Judul Aplikasi
        Text(
            text = "SIM-KADER",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = warnaUtama
        )
        Text(
            text = "Ikatan Mahasiswa Muhammadiyah\nFakultas Teknik UMY",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 3. Card Form Login
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Input Username
                OutlinedTextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.username = it },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = warnaUtama) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input Password
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

                // Pesan Error
                if (viewModel.loginError != null) {
                    Text(
                        text = viewModel.loginError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- REVISI: PASTIKAN PARAMETER SINKRON DENGAN DATA DARI SERVER ---
                Button(
                    onClick = {
                        viewModel.login { roleFromServer, usernameFromServer ->
                            // roleFromServer di sini harus berisi "user" sesuai log
                            onLoginSuccess(roleFromServer, usernameFromServer)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = warnaUtama),
                    enabled = !viewModel.isLoading
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("LOGIN SEKARANG", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 4. Navigasi ke Daftar
        Row(
            modifier = Modifier.padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Belum punya akun?", color = Color.Gray)
            TextButton(onClick = onNavToRegister) {
                Text("Daftar Sekarang", color = warnaUtama, fontWeight = FontWeight.Bold)
            }
        }
    }
}