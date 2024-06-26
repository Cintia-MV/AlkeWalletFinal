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
import com.example.alkewalletfinal.databinding.FragmentSendBinding
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.model.response.TransactionsRequest
import com.example.alkewalletfinal.viewModel.ErroresTransferencia
import com.example.alkewalletfinal.viewModel.SendViewModel
import com.example.alkewalletfinal.viewModel.factory.SendViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SendFragment : Fragment() {
   private lateinit var binding: FragmentSendBinding
   private lateinit var viewModel: SendViewModel
   private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentSendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inicializar AuthManager
        authManager = AuthManager(requireContext())

// Inicializar el ViewModel
        viewModel = ViewModelProvider(this, SendViewModelFactory(authManager)).get(SendViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Observar los errores de transferencia y manejarlos.
        viewModel.transferenciaError.observe(viewLifecycleOwner){error ->
            error?.let {
                val mensajeError = when(it){
                    ErroresTransferencia.MONTO_INVALIDO -> "Ingrese un monto válido"
                    ErroresTransferencia.NOTA_INVALIDA -> "La nota no puede estar vacía"
                    else -> ""
                }
                mostrarMensajeError(mensajeError)
            }
        }

        // Observar el éxito de la transferencia y manejarlo.
        viewModel.transferenciaExitosa.observe(viewLifecycleOwner){exito ->
            if (exito){
                val amount = binding.ingresarMonto.text.toString().toLong()
                val concept = binding.notaEnviar.text.toString()
                val type = "payment"
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

                viewModel.crearTransSend(transaccion)

            }
        }

        viewModel.transactionReult.observe(viewLifecycleOwner){result ->
            if (result){
                mostrarMensajeExito("Transferencia realizada con éxito")
                // Retraso para mostrar el mensaje de éxito antes de navegar de regreso a la página de inicio.
                CoroutineScope(Dispatchers.Main).launch {
                    delay(3000)
                    view.findNavController().navigate(R.id.action_sendFragment_to_homePageFragment)
                }
            } else {
                mostrarMensajeError("Error al realizar la transacción")
            }
        }

        // Botón "Enviar" para realizar la validación del monto y la nota.
        binding.button.setOnClickListener {
            val monto = binding.ingresarMonto.text.toString()
            val nota = binding.notaEnviar.text.toString()
            viewModel.validarTransferencia(monto, nota)


        }

        //Botón para volver a la página principal
        binding.volverSend.setOnClickListener{
            view.findNavController().navigate(R.id.action_sendFragment_to_homePageFragment)
        }

    }

    // Función para mostrar mensajes de error en el texto de la vista
    private fun mostrarMensajeError(mensaje: String){
        binding.errorTextView.text = mensaje
        binding.errorTextView.visibility = View.VISIBLE
    }

    // Función para mostrar mensajes de éxito en un Toast y ocultar el texto de error.
    private fun mostrarMensajeExito(mensaje:String){
        binding.errorTextView.visibility = View.GONE
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
    }
}