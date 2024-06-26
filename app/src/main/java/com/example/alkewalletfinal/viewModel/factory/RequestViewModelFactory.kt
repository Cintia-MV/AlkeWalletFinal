package com.example.alkewalletfinal.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.viewModel.RequestViewModel

class RequestViewModelFactory(private val authManager: AuthManager): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RequestViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RequestViewModel(authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}