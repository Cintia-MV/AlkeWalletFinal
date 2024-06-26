package com.example.alkewalletfinal.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.viewModel.SendViewModel

class SendViewModelFactory(private val authManager: AuthManager): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SendViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SendViewModel(authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}