package com.example.alkewalletfinal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.databinding.FragmentRequestBinding
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.model.response.TransactionsRequest
import com.example.alkewalletfinal.viewModel.ErroresIngresoMonto
import com.example.alkewalletfinal.viewModel.RequestViewModel
import com.example.alkewalletfinal.viewModel.factory.RequestViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Fragmento que permite al usuario realizar una solicitud de ingreso de dinero.
 * Maneja la validación del monto ingresado y la creación de transacciones.
 * También muestra mensajes de éxito o error según el resultado de la transacción.
 */
class RequestFragment : Fragment() {

    private lateinit var binding: FragmentRequestBinding
    private lateinit var viewModel: RequestViewModel
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inicializar AuthManager
        authManager = AuthManager(requireContext())

        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this, RequestViewModelFactory(authManager)).get(RequestViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Observar los errores de ingreso de monto y manejarlos.
        viewModel.ingresoMontoError.observe(viewLifecycleOwner) { error ->
            error?.let {
                val mensajeError = when (it) {
                    ErroresIngresoMonto.MONTO_INVALIDO -> "Ingrese un monto válido (valor numérico)"
                    ErroresIngresoMonto.NOTA_INVALIDA -> "La nota no puede estar vacía"
                    else -> ""
                }
                mostrarErrorMonto(mensajeError)
            }
        }

        // Observar el éxito del ingreso de monto y manejarlo.
        viewModel.ingresoMontoExitoso.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                val amount = binding.ingresarDinero.text.toString().toLong()
                val concept = binding.notaIngresar.text.toString()
                val type = "topup"
                val accountId = 2270L
                val userId = 3548L
                val toAccountId = 2271L

                val transaccion = TransactionsRequest(
                    amount = amount,
                    concept = concept,
                    type = type,
                    accountId = accountId,
                    userId = userId,
                    toAccountId = toAccountId
                )

                viewModel.createTransaction(transaccion)

            }
        }

        // Observar el resultado de la transacción
        viewModel.transactionReult.observe(viewLifecycleOwner){result ->
            if (result){
                mostrarExitoMonto("Transacción realizada con éxito")
                CoroutineScope(Dispatchers.Main).launch {
                    delay(3000)
                    view.findNavController().navigate(R.id.action_requestFragment_to_homePageFragment)
                }
            } else {
                mostrarErrorMonto("Error al realizar la transacción")
            }
        }

        //Boton para para realizar la validación del monto y la nota
        binding.button2.setOnClickListener {
            val monto = binding.ingresarDinero.text.toString()
            val nota = binding.notaIngresar.text.toString()
            viewModel.validarIngresoMonto(monto, nota)
        }

        //Volver a la página anterior
        binding.volverRequest.setOnClickListener {
            view.findNavController().navigate(R.id.action_requestFragment_to_homePageFragment)
        }

    }

    // Función para mostrar mensajes de error en el texto de la interfaz de usuario
    private fun mostrarErrorMonto(mensaje: String) {
        binding.errorTextView.text = mensaje
        binding.errorTextView.visibility = View.VISIBLE
    }

    // Función para mostrar mensajes de éxito en un Toast y ocultar el texto de error.
    private fun mostrarExitoMonto(mensaje: String) {
        binding.errorTextView.visibility = View.VISIBLE
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
    }
}

