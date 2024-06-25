package com.example.alkewalletfinal.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alkewalletfinal.model.AuthManager

class HomeViewModelFactory (private val authManager: AuthManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}