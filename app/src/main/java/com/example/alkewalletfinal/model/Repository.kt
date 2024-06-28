package com.example.alkewalletfinal.model

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.alkewalletfinal.model.local.WalletDao
import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal
import com.example.alkewalletfinal.model.remote.Api
import com.example.alkewalletfinal.model.response.LoginRequest
import com.example.alkewalletfinal.model.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio que gestiona la obtención y almacenamiento de datos desde y hacia la base de datos local y la API remota.
 *
 * @property walletDao DAO para acceder a la base de datos local.
 * @property api Interfaz para realizar llamadas a la API remota.
 * @author Cintia Muñoz V.
 */
class Repository(private val walletDao: WalletDao, private val api: Api) {

    //Obtiene y guarda la información del usuario desde la API.
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

    //Obtiene las cuentas del usuario por su ID desde la base de datos local.
    fun getAccountsByUserId(userId: Long): LiveData<List<AccountsLocal>> {
        return walletDao.getAccountsByUserId(userId)
    }

    //Obtiene las transacciones del usuario por su ID desde la base de datos local.
    fun getTransactionsByUserId(userId: Long): LiveData<List<TransactionsLocal>> {
        return walletDao.getTransactionsByUserId(userId)
    }

    //Obtiene y guarda las cuentas del usuario desde la API.
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

    //Obtiene y guarda las transacciones del usuario desde la API.
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

            }
        }
    }

    //Inicio de sesión del usuario utilizando la API.
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

