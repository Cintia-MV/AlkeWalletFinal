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


class Repository(private val walletDao: WalletDao, private val api: Api) {

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

    fun getTransactionsByUserId(userId: Long): LiveData<List<TransactionsLocal>> {
        return walletDao.getTransactionsByUserId(userId)
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

