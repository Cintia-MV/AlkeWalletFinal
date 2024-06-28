package com.example.alkewalletfinal.viewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.model.Repository
import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal
import com.example.alkewalletfinal.model.remote.RetrofitClient
import com.example.alkewalletfinal.model.response.UserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel(private val authManager: AuthManager, private val repository: Repository): ViewModel() {

    var userId: Long? = null

    private val _userResponseInfo: MutableLiveData<UserResponse> = MutableLiveData()
    val userResponseInfo: LiveData<UserResponse> get() = _userResponseInfo
    val accountsResponse: MutableLiveData<List<AccountsLocal>> = MutableLiveData()
    val transactionsResponseLiveData: MutableLiveData<List<TransactionsLocal>> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getUserData() {
        val token = authManager.getToken()

        token?.let { authToken ->
            CoroutineScope(Dispatchers.IO).launch {
                try {

                    fetchAndSaveAccounts("Bearer $authToken")

                    fetchAndSaveTransactions("Bearer $authToken")

                } catch (e: Exception){
                    Log.e("HomeViewModel", "Error fetching user data: ${e.message}")
                    errorLiveData.postValue("Error fetching user data: ${e.message}")
                }
            }

        }
    }


    private suspend fun fetchAndSaveAccounts(token: String) {
        try {
            repository.fetchAndSaveAccounts(token)
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Exception fetching accounts: ${e.message}")
            errorLiveData.postValue("Exception fetching accounts: ${e.message}")
        }
    }


    private suspend fun fetchAndSaveTransactions(token: String) {
        try {
            repository.fetchAndSaveTransactions(token)
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Exception fetching transactions: ${e.message}")
            errorLiveData.postValue("Exception fetching transactions: ${e.message}")
        }
    }

    fun fetchUserInfo() {
        val token = authManager.getToken()

        token?.let { authToken ->
            RetrofitClient.retrofitInstance(authToken).infoUser("Bearer $authToken")
                .enqueue(object : Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let { userResponse ->
                                _userResponseInfo.postValue(userResponse)
                                Log.d("HomeViewModel", "Información del usuario: ${userResponse.id}, ${userResponse.firstName}, ${userResponse.lastName}")

                                // Almacenar el userId y observar cuentas
                                userId = userResponse.id
                                observeAccounts(userResponse.id)
                                observeTransactions(userResponse.id)
                            }
                        } else {
                            errorLiveData.postValue("Error: ${response.code()} ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        errorLiveData.postValue("Error: ${t.message}")
                    }
                })
        }
    }


    private fun observeAccounts(userId: Long) {
        repository.getAccountsByUserId(userId).observeForever { accounts ->
            accountsResponse.postValue(accounts)
        }
    }

    private fun observeTransactions(userId: Long) {
        repository.getTransactionsByUserId(userId).observeForever { transactions ->
            transactionsResponseLiveData.postValue(transactions)
        }
    }

    fun clearUserToken() {
        authManager.clearToken()
    }

}