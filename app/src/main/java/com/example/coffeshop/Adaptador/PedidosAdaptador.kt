package com.example.coffeshop.Adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeshop.Modelo.dataPedidos
import java.util.Locale

class PedidosAdaptador (private val lista: List<dataPedidos>):
    RecyclerView.Adapter<PedidosAdaptador.PedidoViewHolder>() {
    inner class PedidoViewHolder(val binding: ItemPedidoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val b = ItemPedidoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PedidoViewHolder(b)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = lista[position]
        holder.binding.txtNumero.text = "Pedido #${pedido.id_pedido}"
        holder.binding.txtEstado.text = pedido.estado.replace("_", " ")
        holder.binding.txtFecha.text = pedido.fecha
        holder.binding.txtTotal.text =
            String.format(Locale.getDefault(), "$ %.2f", pedido.total)
    }

    override fun getItemCount() = lista.size

}