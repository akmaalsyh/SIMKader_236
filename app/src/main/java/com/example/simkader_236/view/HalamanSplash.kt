package com.example.simkader_236.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simkader_236.R
import kotlinx.coroutines.delay

@Composable
fun HalamanSplash(
    onTimeout: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Tambahkan state untuk skala/zoom
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) } // Mulai dari ukuran 80%

    LaunchedEffect(Unit) {
        // Jalankan animasi Fade-In dan Zoom-In secara bersamaan
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )
        scale.animateTo(
            targetValue = 1f, // Berhenti di ukuran normal (100%)
            animationSpec = tween(durationMillis = 1000)
        )

        delay(1500) // Memberikan waktu user melihat loading
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB71C1C)), // Warna Merah Maroon IMM
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .alpha(alpha.value)
                .scale(scale.value), // Terapkan animasi Zoom-In
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_imm),
                contentDescription = "Logo IMM FT UMY",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "SIM-KADER",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "IMM FT UMY",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 2. Tambahkan Indikator Loading di bagian bawah
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
        }

        // Teks Versi di paling bawah layar
        Text(
            text = "Versi 1.0.0",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}