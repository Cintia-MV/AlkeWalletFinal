package com.example.alkewalletfinal.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alkewalletfinal.HistorialAdapter
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.databinding.FragmentHomePageBinding
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.viewModel.HomeViewModel
import com.example.alkewalletfinal.viewModel.HomeViewModelFactory


class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var authManager: AuthManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       authManager = AuthManager(requireContext())
        viewModel = ViewModelProvider(this, HomeViewModelFactory(authManager)).get(HomeViewModel::class.java)

        viewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer { userResponse ->


           userResponse?.let {
               binding.saludoP5.text = "Hola, ${userResponse.firstName} ${userResponse.lastName}"
               Log.d("HomePageFragment", "User: ${userResponse.firstName} ${userResponse.lastName}")
           }
        })

        viewModel.accountsResponse.observe(viewLifecycleOwner, Observer { cuenta ->
            cuenta?.let {
                val cuentas = cuenta[0]
                binding.saldoPesosP5.text = "$ ${cuentas.money}"
                Log.d("HomePageFragment", "Money: ${cuentas.money}")
            }
        })

        val adapter = HistorialAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.transactionsResponseLiveData.observe(viewLifecycleOwner, Observer {transaction ->
            transaction?.let {
                adapter.actualizar(it)
            }
        })

        viewModel.getUserData()

        //HASTA AQUÍ VOY BIEN********************************************

        //Clic en la imagen para navegar hacia la configuración del perfil
       /* binding.imgArnold.setOnClickListener{
            findNavController().navigate(R.id.action_homePageFragment2_to_profileFragment)
        }

        //Clic en botón enviar dinero para navegar hacia la transacción
        hPBinding.btnEnviarDinP5.setOnClickListener {
            view.findNavController().navigate(R.id.action_homePageFragment2_to_sendFragment)
        }

        hPBinding.btnIngresarDinP5.setOnClickListener {
            view.findNavController().navigate(R.id.action_homePageFragment2_to_requestFragment)
        }*/
    }


}
