package com.example.alkewalletfinal.model

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.alkewalletfinal.model.local.WalletDao
import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal
import com.example.alkewalletfinal.model.local.entities.UsuarioLocal
import com.example.alkewalletfinal.model.remote.Api
import com.example.alkewalletfinal.model.remote.RetrofitClient
import com.example.alkewalletfinal.model.response.AccountsResponse
import com.example.alkewalletfinal.model.response.LoginRequest
import com.example.alkewalletfinal.model.response.LoginResponse
import com.example.alkewalletfinal.model.response.TransactionsRequest
import com.example.alkewalletfinal.model.response.TransactionsResponse
import com.example.alkewalletfinal.model.response.UserRequest
import com.example.alkewalletfinal.model.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val walletDao: WalletDao, private val api: Api) {
    suspend fun insertUser(usuario: UsuarioLocal) {
        walletDao.insertUser(usuario)
    }

    fun getUser(id: Long): LiveData<UsuarioLocal> {
        return walletDao.getUsuarios(id)
    }

    suspend fun getUserId(): Long? {
        return walletDao.getUserId()
    }


    suspend fun fetchAndSaveUser(token: String) {
        withContext(Dispatchers.IO) {
            val response = api.infoUser(token).execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    val usuarioLocal = usuarioResponseInter(it)
                    walletDao.insertUser(usuarioLocal)
                }
            }
        }
    }

    fun getAccountsByUserId(userId: Long): LiveData<List<AccountsLocal>> {
        return walletDao.getAccountsByUserId(userId)
    }

    suspend fun insertAccounts(cuenta: AccountsLocal) {
        walletDao.insertAccounts(cuenta)
    }

    fun getAccounts(id: Long): LiveData<AccountsLocal> {
        return walletDao.getCuentas(id)
    }

    suspend fun fetchAndSaveAccounts(token: String) {
        withContext(Dispatchers.IO) {
            val response = api.infoAccounts(token).execute()
            if (response.isSuccessful) {
                response.body()?.forEach {
                    val accountLocal = cuentaResponseInter(it)
                    walletDao.insertAccounts(accountLocal)
                }
            }
        }
    }

    suspend fun insertTransaction(transaccion: TransactionsLocal) {
        walletDao.insertTransaction(transaccion)
    }

    fun getTransactions(id: Long): LiveData<TransactionsLocal> {
        return walletDao.getTransactions(id)
    }

    suspend fun fetchAndSaveTransactions(token: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.infoTransactions(token).execute()
                if (response.isSuccessful) {
                    val transactionResponse = response.body()
                    if (transactionResponse != null) {
                        transactionResponse.data.forEach { transaction ->
                            val transactionLocal = transactionResponseInter(transaction)
                            walletDao.insertTransaction(transactionLocal)
                        }
                    } else {
                        Log.e("WalletRepository", "Empty response body")
                    }
                } else {
                    Log.e("WalletRepository", "Error fetching transactions: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("WalletRepository", "Exception fetching transactions: ${e.message}")
                // Manejar cualquier excepción que pueda ocurrir durante la ejecución
            }
        }
    }


    // Crear usuario en el servidor
    suspend fun createUserOnServer(userRequest: UserRequest) {
        withContext(Dispatchers.IO) {
            val response = api.createUser(userRequest).execute()
            if (response.isSuccessful) {
                // Manejar la respuesta exitosa si es necesario
            }
        }
    }

    // Crear transacción en el servidor
    suspend fun createTransactionOnServer(token: String, transactionRequest: TransactionsRequest) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.createTransaction(token, transactionRequest).execute()
                if (response.isSuccessful) {
                    val transactionResponse = response.body()
                    if (transactionResponse != null) {
                        transactionResponse.data.forEach { transaction ->
                            val transactionLocal = transactionResponseInter(transaction)
                            walletDao.insertTransaction(transactionLocal)
                        }
                    } else {
                        Log.e("WalletRepository", "Empty response body")
                    }
                } else {
                    Log.e("WalletRepository", "Error creating transaction: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("WalletRepository", "Exception creating transaction: ${e.message}")
                // Aquí puedes manejar cualquier excepción que ocurra durante la ejecución
            }
        }
    }

    suspend fun login(loginRequest: LoginRequest): LoginResponse? {
        return withContext(Dispatchers.IO) {
            val response = api.login(loginRequest).execute()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }

    }
}

