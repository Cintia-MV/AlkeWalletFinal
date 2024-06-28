package com.example.alkewalletfinal.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.databinding.FragmentLoginBinding
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.model.Repository
import com.example.alkewalletfinal.model.local.WalletDataBase
import com.example.alkewalletfinal.model.remote.RetrofitClient
import com.example.alkewalletfinal.viewModel.ErroresLogin
import com.example.alkewalletfinal.viewModel.LoginViewModel
import com.example.alkewalletfinal.viewModel.factory.LoginViewModelFactory


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inicializar AuthManager
        val authManager = AuthManager(requireContext())

        // Inicializa el WalletDao desde la base de datos.
        val walletDao = WalletDataBase.getDataBase(requireContext()).getWalletDao()

        val apiService = RetrofitClient.retrofitInstance()
        // Inicializa el Repository con el WalletDao y el AuthManager.
        val repository = Repository(walletDao, apiService)

        // Inicializa el ViewModel.
        viewModel = ViewModelProvider(this, LoginViewModelFactory(requireContext(), repository))
            .get(LoginViewModel::class.java)
        // Asigna el ViewModel al binding y establece el lifecycleOwner.
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        // Observa los cambios en los errores de login.
        viewModel.loginError.observe(viewLifecycleOwner){ error ->
            when(error){
                ErroresLogin.emailNoValido -> {
                    binding.errorTextView.text = "Ingresar correo electrónico válido"
                    binding.errorTextView.visibility = View.VISIBLE
                }
                ErroresLogin.claveNoValida ->{
                    binding.errorTextView.text = "La contraseña debe tener almenos 6 caracteres"
                    binding.errorTextView.visibility = View.VISIBLE
                }
                ErroresLogin.credencialesIncorrectas -> {
                    binding.errorTextView.text = "Email o contraseña incorrectos"
                    binding.errorTextView.visibility = View.VISIBLE
                }
                ErroresLogin.errorDeRed -> {
                    binding.errorTextView.text = "Error de red, por favor intente nuevamente"
                    binding.errorTextView.visibility = View.VISIBLE
                }
                null -> {
                    binding.errorTextView.visibility = View.GONE
                    Toast.makeText(requireContext(), "¡Sesión iniciada con éxito!", Toast.LENGTH_LONG).show()

                    // Obtener el ID del usuario después del inicio de sesión exitoso
                    val userId = viewModel.getUserId()
                    Log.d("LoginFragment", "ID de usuario: $userId")
                    // Navegar al fragmento HomePage
                    findNavController().navigate(R.id.action_loginFragment_to_homePageFragment)

                }
            }
        }


        //Botón para iniciar sesión
        binding.btnP3.setOnClickListener {
            val  email = binding.hintEmailP3.text.toString()
            val clave = binding.claveHintP3.text.toString()
            viewModel.validarUsuario(email, clave)
        }


        //Botón para crear una nueva cuenta
        binding.crearCtaP3.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

}