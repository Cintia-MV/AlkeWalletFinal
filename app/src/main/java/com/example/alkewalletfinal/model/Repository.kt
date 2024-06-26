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

class Repository(private val walletDao: WalletDao, private val authManager: AuthManager) {

    //Obtener transacciones locales
    fun getLocalTransactions(id: Long): LiveData<TransactionsLocal>{
        return walletDao.getTransactions(id)
    }

    // Insertar transacción localmente
    private suspend fun insertLocalTransaction(transaction: TransactionsLocal){
        withContext(Dispatchers.IO){
            walletDao.insertTransaction(transaction)
        }
    }

    //Crear transaccion en el servidor
    fun createTransaction(transaction: TransactionsRequest, callback: (Boolean, String?) -> Unit){
        val token = authManager.getToken()

        if (token != null){
            val authToken = "Bearer $token"
            RetrofitClient.retrofitInstance(token).createTransaction(authToken, transaction)
                .enqueue(object : Callback<TransactionsResponse>{
                    override fun onResponse(
                        call: Call<TransactionsResponse>,
                        response: Response<TransactionsResponse>
                    ) {
                        if (response.isSuccessful){
                            response.body()?.let {
                                val transactionLocal = transactionResponseInter(it.data[0])
                                insertTransactionLocally(transactionLocal)
                                callback(true, null)
                            } ?: run {
                                callback(false, "Response body is null")
                            }
                        } else {
                            callback(false, "Response is not successful")
                        }
                    }

                    override fun onFailure(call: Call<TransactionsResponse>, t: Throwable) {
                        callback(false, t.message)
                    }
                })
        } else {
            callback(false, "Token is null")
        }
    }

    //Otros métodos


    private fun insertTransactionLocally(transaction: TransactionsLocal){
        kotlinx.coroutines.GlobalScope.launch {
            insertLocalTransaction(transaction)
        }
    }


    // Obtener usuario local
    fun getLocalUser(id: Long): LiveData<UsuarioLocal> {
        return walletDao.getUsuarios(id)
    }

    // Insertar usuario localmente
    suspend fun insertLocalUser(usuario: UsuarioLocal) {
        withContext(Dispatchers.IO) {
            walletDao.insertUser(usuario)
        }
    }

    // Método auxiliar para insertar usuario localmente
    private fun insertUserLocally(usuario: UsuarioLocal) {
        GlobalScope.launch {
            insertLocalUser(usuario)
        }
    }

    // Obtener cuentas locales
    fun getLocalAccounts(id: Long): LiveData<AccountsLocal> {
        return walletDao.getCuentas(id)
    }


    // Insertar cuentas localmente
    suspend fun insertLocalAccounts(cuenta: AccountsLocal) {
        withContext(Dispatchers.IO) {
            walletDao.insertAccounts(cuenta)
        }
    }

    // Método auxiliar para insertar cuenta localmente
    private fun insertAccountLocally(cuenta: AccountsLocal) {
        GlobalScope.launch {
            insertLocalAccounts(cuenta)
        }
    }

    fun fetchUsuario(callback: (Boolean, String?) -> Unit){
        val token = authManager.getToken()

        if (token != null){
            val authToken = "Bearer $token"

            RetrofitClient.retrofitInstance(token).infoUser(authToken)
                .enqueue(object : Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if(response.isSuccessful){
                            response.body()?.let {
                                val usuarioLocal = usuarioResponseInter(it)
                                insertUserLocally(usuarioLocal)
                                callback(true, null)
                            } ?: run {
                                callback(false, "Response body is null")
                            }
                        } else {
                            callback(false, "Response is not successful")
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        callback(false, t.message)
                    }
                })
        } else {
            callback(false, "Token is null")
        }
    }

    // Método para iniciar sesión en el servidor
    fun login(loginRequest: LoginRequest, callback: (Boolean, LoginResponse?, String?) -> Unit) {
        val api = RetrofitClient.retrofitInstance()
        api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
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
    }


   fun fetchCuenta(callback: (Boolean, String?) -> Unit) {
        val token = authManager.getToken()

        if (token != null) {
            val authToken = "Bearer $token"
            RetrofitClient.retrofitInstance(token).infoAccounts(authToken)
                .enqueue(object : Callback<List<AccountsResponse>> {
                    override fun onResponse(
                        call: Call<List<AccountsResponse>>,
                        response: Response<List<AccountsResponse>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                it.forEach { accountResponse ->
                                    val accountLocal = cuentaResponseInter(accountResponse)
                                    insertAccountLocally(accountLocal)
                                }
                                callback(true, null)
                            } ?: run {
                                callback(false, "Response body is null")
                            }
                        } else {
                            callback(false, "Response is not successful")
                        }
                    }

                    override fun onFailure(call: Call<List<AccountsResponse>>, t: Throwable) {
                        callback(false, t.message)
                    }
                })
        } else {
            callback(false, "Token is null")
        }
    }
}