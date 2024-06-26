package com.example.alkewalletfinal.model

import androidx.lifecycle.LiveData
import com.example.alkewalletfinal.model.local.WalletDao
import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal
import com.example.alkewalletfinal.model.local.entities.UsuarioLocal
import com.example.alkewalletfinal.model.remote.RetrofitClient
import com.example.alkewalletfinal.model.response.AccountsResponse
import com.example.alkewalletfinal.model.response.LoginRequest
import com.example.alkewalletfinal.model.response.LoginResponse
import com.example.alkewalletfinal.model.response.TransactionsRequest
import com.example.alkewalletfinal.model.response.TransactionsResponse
import com.example.alkewalletfinal.model.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface Repository {
    suspend fun insertUser(usuario: UserResponse)
    fun getUser(id: Long): LiveData<UsuarioLocal>
    suspend fun insertAccounts(cuenta: AccountsLocal)
    fun getAccounts(id: Long): LiveData<AccountsLocal>
    suspend fun insertTransaction(transaccion: TransactionsLocal)
    fun getTransactions(id: Long): LiveData<TransactionsLocal>
    suspend fun login(
        loginRequest: LoginRequest,
        callback: (Boolean, LoginResponse?, String?) -> Unit
    )
    suspend fun getUserInfo(token: String): UserResponse

}