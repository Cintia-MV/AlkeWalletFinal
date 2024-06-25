package com.example.alkewalletfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alkewalletfinal.databinding.ItemListBinding
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal

class HistorialAdapter: RecyclerView.Adapter<HistorialAdapter.ListaTransaccionesVH>() {
    private var listaTransaccion = listOf<TransactionsLocal>()
    private val transaccionSelected = MutableLiveData<TransactionsLocal>()


    fun actualizar(transaccion: List<TransactionsLocal>){
        listaTransaccion = transaccion
        notifyDataSetChanged()
    }

    fun seleccionarTransaccion(): LiveData<TransactionsLocal> = transaccionSelected


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaTransaccionesVH {
        return ListaTransaccionesVH(ItemListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount() = listaTransaccion.size

    override fun onBindViewHolder(holder: ListaTransaccionesVH, position: Int) {
        val transaccion = listaTransaccion[position]
        holder.bind(transaccion)
    }


    //Clase interna
    inner class ListaTransaccionesVH(private val binding: ItemListBinding):
            RecyclerView.ViewHolder(binding.root), View.OnClickListener{

                fun bind(transaccion: TransactionsLocal){
                    Glide.with(binding.imgUsuario).load(R.drawable.foto3).centerCrop().into(binding.imgUsuario)
                    binding.nombreUsuario.text = transaccion.concept
                    binding.fecha.text = transaccion.date
                    binding.monto.text = transaccion.amount

                    itemView.setOnClickListener(this)
                }



        override fun onClick(v: View?) {
            transaccionSelected.value = listaTransaccion[adapterPosition]
        }
    }
}