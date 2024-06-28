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
import com.example.alkewalletfinal.model.Repository
import com.example.alkewalletfinal.model.local.WalletDao
import com.example.alkewalletfinal.model.local.WalletDataBase
import com.example.alkewalletfinal.model.remote.Api
import com.example.alkewalletfinal.model.remote.RetrofitClient
import com.example.alkewalletfinal.viewModel.HomeViewModel
import com.example.alkewalletfinal.viewModel.factory.HomeViewModelFactory

/**
 * Fragmento principal de la página de inicio que muestra la información del usuario,
 * cuentas y transacciones, y proporciona navegación a otras partes de la aplicación.
 */
class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var authManager: AuthManager
    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())

        // Obtén el token del AuthManager
        val token = authManager.getToken()

        // Inicializa la API con Retrofit
        val api: Api = RetrofitClient.retrofitInstance(token)

        // Inicializa Room para la base de datos local
        val walletDao: WalletDao = WalletDataBase.getDataBase(requireContext()).getWalletDao()

        var userId : Long = 0

        // Inicializa Repository con las instancias de walletDao y api
        repository = Repository(walletDao, api)

        // Inicializar el ViewModel con el Factory personalizado
        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(authManager, repository)
        ).get(HomeViewModel::class.java)

        // Observar la información del usuario para actualizar la vista cuando cambie
        viewModel.userResponseInfo.observe(viewLifecycleOwner, Observer { userResponse ->

            userResponse?.let {
                binding.saludoP5.text = "Hola, ${userResponse.firstName} ${userResponse.lastName}"
                userId = userResponse.id
                Log.d(
                    "HomePageFragment",
                    "User: ${userResponse.firstName} ${userResponse.lastName}"

                )
                Log.d("IdUsuario", userId.toString())
            }
        })

        // Observar la lista de cuentas del usuario para actualizar la vista cuando cambie
        viewModel.accountsResponse.observe(viewLifecycleOwner, Observer { cuentas ->
            cuentas?.let {
                val userAccounts = cuentas.filter { it.userId == userId }
                if (userAccounts.isNotEmpty()) {
                    val account = userAccounts[0]
                    binding.saldoPesosP5.text = "$ ${account.money}"

                    Log.d("HomePageFragment", "Money: ${account.money}")
                } else {
                    Log.d("HomePageFragment", "No accounts found for userId: $userId")
                }
            }
        })

        // Configurar el adaptador para el RecyclerView que muestra el historial de transacciones
        val adapter = HistorialAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observar la lista de transacciones para actualizar el RecyclerView cuando cambie
        viewModel.transactionsResponseLiveData.observe(viewLifecycleOwner, Observer { transaction ->
            transaction?.let {
                adapter.actualizar(it)
            }
        })

        // Iniciar la obtención de datos del usuario y las actualizaciones de información al cargar el fragmento
        viewModel.getUserData()
        viewModel.fetchUserInfo()


        //Clic en la imagen para navegar hacia la configuración del perfil
        binding.imgArnold.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_profileFragment)
        }

        //Clic en botón enviar dinero para navegar hacia la transacción
        binding.btnEnviarDinP5.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_sendFragment)
        }

        //Clic en botón enviar dinero para navegar hacia el depósito
        binding.btnIngresarDinP5.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_requestFragment)
        }
    }

}
