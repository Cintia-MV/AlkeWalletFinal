package com.example.alkewalletfinal.viewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.model.Repository
import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal
import com.example.alkewalletfinal.model.local.entities.UsuarioLocal
import com.example.alkewalletfinal.model.remote.RetrofitClient
import com.example.alkewalletfinal.model.response.AccountsResponse
import com.example.alkewalletfinal.model.response.Transactions
import com.example.alkewalletfinal.model.response.TransactionsResponse
import com.example.alkewalletfinal.model.response.UserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val authManager: AuthManager, private val repository: Repository): ViewModel() {

    private val _userResponseLiveData: MutableLiveData<UsuarioLocal> = MutableLiveData()
    val userResponseLiveData: LiveData<UsuarioLocal> get() = _userResponseLiveData
    val accountsResponse: MutableLiveData<List<AccountsLocal>> = MutableLiveData()
    val transactionsResponseLiveData: MutableLiveData<List<TransactionsLocal>> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getUserData() {
        val token = authManager.getToken()

        token?.let { authToken ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    fetchUser("Bearer $authToken")

                    fetchAndSaveAccounts("Bearer $authToken")

                    fetchAndSaveTransactions("Bearer $authToken")
                } catch (e: Exception){
                    Log.e("HomeViewModel", "Error fetching user data: ${e.message}")
                    errorLiveData.postValue("Error fetching user data: ${e.message}")
                }
            }

        }
    }

    private suspend fun fetchUser(token: String) {
        try {
            repository.fetchAndSaveUser(token)

        } catch (e: Exception) {
            Log.e("HomeViewModel", "Exception fetching user: ${e.message}")
            errorLiveData.postValue("Exception fetching user: ${e.message}")
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


    fun clearUserToken() {
        authManager.clearToken()
        authManager.clearUserLogged()
    }

}