package com.example.simkader_236.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
    // State untuk animasi efek masuk (Fade-In)
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Efek animasi muncul selama 1.5 detik
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )
        // Tunggu sebentar setelah animasi selesai sebelum pindah halaman
        delay(1500)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB71C1C)), // BACKGROUND MERAH
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.alpha(alpha.value), // TERAPKAN EFEK ANIMASI DI SINI
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
                color = Color.White, // TULISAN PUTIH
                textAlign = TextAlign.Center
            )

            Text(
                text = "IMM FT UMY",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f), // TULISAN PUTIH SEDIKIT TRANSPARAN
                textAlign = TextAlign.Center
            )
        }
    }
}