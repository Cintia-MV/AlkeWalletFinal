package com.example.alkewalletfinal.viewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.model.Repository
import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal
import com.example.alkewalletfinal.model.local.entities.UsuarioLocal
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

    val userLiveData: MutableLiveData<UserResponse> = MutableLiveData()
    private val _userResponseLiveData: MutableLiveData<UsuarioLocal> = MutableLiveData()
    private val _userResponseInfo: MutableLiveData<UserResponse> = MutableLiveData()

    val userResponseInfo: LiveData<UserResponse> get() = _userResponseInfo
    //val userResponseInfo: MutableLiveData<UserResponse> = MutableLiveData()
    val userResponseLiveData: LiveData<UsuarioLocal> get() = _userResponseLiveData
    val accountsResponse: MutableLiveData<List<AccountsLocal>> = MutableLiveData()
    val transactionsResponseLiveData: MutableLiveData<List<TransactionsLocal>> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getUserData(userId: Long) {
        val token = authManager.getToken()

        token?.let { authToken ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    //fetchUser("Bearer $authToken")

                    fetchAndSaveAccounts("Bearer $authToken")

                    fetchAndSaveTransactions("Bearer $authToken")
                   // observeAccounts(userId)
                } catch (e: Exception){
                    Log.e("HomeViewModel", "Error fetching user data: ${e.message}")
                    errorLiveData.postValue("Error fetching user data: ${e.message}")
                }
            }

        }
    }

   /* private suspend fun fetchUser(token: String) {
        try {
            repository.fetchAndSaveUser(token)

            // Utiliza el DAO para obtener los datos del usuario por ID
            repository.getUser().observeForever { user ->
                user?.let {
                    _userResponseLiveData.postValue(user)
                }
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Exception fetching user: ${e.message}")
            errorLiveData.postValue("Exception fetching user: ${e.message}")
        }
    }*/


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


    fun clearUserToken() {
        authManager.clearToken()
        authManager.clearUserLogged()
    }

}