package com.example.alkewalletfinal.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.model.remote.RetrofitClient
import com.example.alkewalletfinal.model.response.TransactionsRequest
import com.example.alkewalletfinal.model.response.TransactionsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ViewModel para manejar la lógica relacionada con el envío de transferencias.
 *
 * Este ViewModel gestiona la validación de monto y nota de transferencia, así como la creación de transacciones a través de Retrofit.
 *
 * @property authManager Gestor de autenticación para obtener y manejar el token de autenticación.
 */
enum class ErroresTransferencia {
    MONTO_INVALIDO,
    NOTA_INVALIDA
}
class SendViewModel(private val authManager: AuthManager) : ViewModel() {
    // LiveData para observar el estado de los errores
    private val _transferenciaError = MutableLiveData<ErroresTransferencia?>()
    val transferenciaError: LiveData<ErroresTransferencia?> = _transferenciaError

    // LiveData para observar el éxito
    private val _transferenciaExitosa = MutableLiveData<Boolean>()
    val transferenciaExitosa: LiveData<Boolean> = _transferenciaExitosa

    // LiveData para observar el resultado de la transacción
    private val _transactionResult = MutableLiveData<Boolean>()
    val transactionReult: LiveData<Boolean> = _transactionResult

    // LiveData para observar errores generales
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    // Método para validar la transferencia.
    fun validarTransferencia(monto:String, nota: String){
        if (!validarMonto(monto)){
            _transferenciaError.value = ErroresTransferencia.MONTO_INVALIDO
            return
        }
        if (!validarNota(nota)){
            _transferenciaError.value = ErroresTransferencia.NOTA_INVALIDA
            return
        }

        //Si todos los campos son válidos, realizar la transferencia cómo exitosa
        _transferenciaError.value = null
        _transferenciaExitosa.value = true
    }

    //Método privado para validar el formato del monto ingresado.
    private fun validarMonto(monto: String): Boolean{
        val montoRegex = Regex("^[0-9]+(\\.[0-9]{1,2})?\$")
        return montoRegex.matches(monto)
    }

    //Método privado para validar que la nota ingresada no esté en blanco.
    private fun validarNota(nota: String):Boolean{
        return nota.isNotBlank()
    }

    //Método para crear una transacción utilizando Retrofit.
    fun crearTransSend(transaccion: TransactionsRequest){
        val token = authManager.getToken()

        token?.let { authToken ->
            RetrofitClient.retrofitInstance(authToken).createTransaction("Bearer $authToken",transaccion)
                .enqueue(object : Callback<TransactionsResponse> {
                    override fun onResponse(
                        call: Call<TransactionsResponse>, response: Response<TransactionsResponse>
                    ) {
                       if (response.isSuccessful){
                           _transactionResult.postValue(true)
                       }else{
                           _transactionResult.postValue(false)
                           _errorLiveData.postValue("Error: ${response.code()} ${response.message()}")
                       }
                    }

                    override fun onFailure(call: Call<TransactionsResponse>, t: Throwable) {
                        _transactionResult.postValue(false)
                        _errorLiveData.postValue("Error: ${t.message}")
                    }
                })
        }
    }
}
