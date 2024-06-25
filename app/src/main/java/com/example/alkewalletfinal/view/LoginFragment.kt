package com.example.alkewalletfinal.view

import android.os.Bundle
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
import com.example.alkewalletfinal.viewModel.ErroresLogin
import com.example.alkewalletfinal.viewModel.LoginViewModel
import com.example.alkewalletfinal.viewModel.LoginViewModelFactory


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


        // Inicializa el ViewModel.
        viewModel = ViewModelProvider(this, LoginViewModelFactory(requireContext()))
            .get(LoginViewModel::class.java)
        // Asigna el ViewModel al binding y establece el lifecycleOwner.
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

       /* viewModel.loginResponse.observe(viewLifecycleOwner){loginResponse ->
            loginResponse?.accessToken?.let { accessToken ->
                val bundle = Bundle().apply {
                    putString("accessToken", accessToken)
                }

            }
        }*/

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