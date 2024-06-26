package com.example.alkewalletfinal.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.viewModel.HomeViewModel

class HomeViewModelFactory (
    private val authManager: AuthManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}