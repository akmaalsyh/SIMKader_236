package com.example.simkader_236.viewmodel.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.simkader_236.repositori.AplikasiDataKader
import com.example.simkader_236.viewmodel.*

object PenyediaViewModel {
    val Factory = viewModelFactory {
        // --- VIEWMODEL UNTUK DASHBOARD & STATISTIK ---
        initializer {
            HomeViewModel(aplikasiDataKader().container.repositoriDataKader)
        }

        initializer {
            GrafikKaderViewModel(aplikasiDataKader().container.repositoriDataKader)
        }

        // --- VIEWMODEL UNTUK MANAJEMEN DATA (CRUD) ---
        initializer {
            ListKaderViewModel(aplikasiDataKader().container.repositoriDataKader)
        }

        initializer {
            EntryViewModel(aplikasiDataKader().container.repositoriDataKader)
        }

        initializer {
            // Membutuhkan SavedStateHandle untuk menangkap ID dari Navigasi
            DetailViewModel(
                this.createSavedStateHandle(),
                aplikasiDataKader().container.repositoriDataKader
            )
        }

        initializer {
            // Membutuhkan SavedStateHandle untuk menangkap ID dari Navigasi
            EditViewModel(
                this.createSavedStateHandle(),
                aplikasiDataKader().container.repositoriDataKader
            )
        }

        // --- VIEWMODEL UNTUK AUTHENTICATION ---
        initializer {
            LoginViewModel(aplikasiDataKader().container.repositoriDataKader)
        }

        initializer {
            RegisterViewModel(aplikasiDataKader().container.repositoriDataKader)
        }
    }
}

/**
 * Fungsi ekstensi untuk memudahkan akses ke [AplikasiDataKader]
 * yang menampung [AppContainer]
 */
fun CreationExtras.aplikasiDataKader(): AplikasiDataKader =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AplikasiDataKader)