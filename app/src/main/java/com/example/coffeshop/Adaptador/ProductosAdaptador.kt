package com.example.coffeshop.Adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeshop.Modelo.dataProductos
import java.util.Locale
import com.example.coffeshop.databinding.ItemProductoBinding

class ProductosAdaptador(private val lista: List<dataProductos>, private val click: (dataProductos) -> Unit
) : RecyclerView.Adapter<ProductosAdaptador.ProductoViewHolder>() {
    inner class ProductoViewHolder(val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = lista[position]
        holder.binding.txtNombre.text = producto.nombre
        holder.binding.txtDescripcion.text = producto.descripcion ?: ""
        holder.binding.txtPrecio.text = String.format(Locale.getDefault(), "$ %.2f", producto.precio)
        holder.binding.btnAgregar.setOnClickListener { click(producto) }
        holder.binding.root.setOnClickListener { click(producto) }
    }

    override fun getItemCount() = lista.size
}