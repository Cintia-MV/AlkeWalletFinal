package com.example.alkewalletfinal.model

import androidx.lifecycle.LiveData
import com.example.alkewalletfinal.model.local.WalletDao
import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal
import com.example.alkewalletfinal.model.local.entities.UsuarioLocal
import com.example.alkewalletfinal.model.remote.Api
import com.example.alkewalletfinal.model.remote.RetrofitClient
import com.example.alkewalletfinal.model.response.LoginRequest
import com.example.alkewalletfinal.model.response.LoginResponse
import com.example.alkewalletfinal.model.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryImpl(private val walletDao: WalletDao, private val apiService: Api) : Repository {
    override suspend fun insertUser(usuarioResponse: UserResponse) {
        val usuarioLocal = usuarioResponseInter(usuarioResponse)
        walletDao.insertUser(usuarioLocal)
    }

    override fun getUser(id: Long): LiveData<UsuarioLocal> {
        return walletDao.getUsuarios(id)
    }

    override suspend fun insertAccounts(cuenta: AccountsLocal) {
        walletDao.insertAccounts(cuenta)
    }

    override fun getAccounts(id: Long): LiveData<AccountsLocal> {
        return walletDao.getCuentas(id)
    }

    override suspend fun insertTransaction(transaccion: TransactionsLocal) {
        walletDao.insertTransaction(transaccion)
    }

    override fun getTransactions(id: Long): LiveData<TransactionsLocal> {
        return walletDao.getTransactions(id)
    }

    override suspend fun login(
        loginRequest: LoginRequest,
        callback: (Boolean, LoginResponse?, String?) -> Unit
    ) {
        try {
            // Realizar la llamada de red usando Retrofit
            apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        callback(true, response.body(), null)
                    } else {
                        callback(false, null, "Response is not successful")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    callback(false, null, t.message)
                }
            })
        } catch (e: Exception) {
            callback(false, null, e.message)
        }


    }

    override suspend fun getUserInfo(token: String): UserResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.infoUser("Bearer $token").execute()
                if (response.isSuccessful) {
                    response.body()
                        ?: throw Exception("No se pudo obtener la información del usuario")
                } else {
                    throw Exception("Error al obtener la información del usuario: ${response.code()}")
                }
            } catch (e: Exception) {
                throw Exception("Error en la solicitud de información del usuario: ${e.message}")
            }
        }
    }
}



