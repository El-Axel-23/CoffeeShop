package com.example.coffeshop.Adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeshop.Modelo.NotificacionItem
import com.example.coffeshop.R

class NotificacionAdapter(
    private var notificaciones: List<NotificacionItem>,
    private val onNotificacionClick: (NotificacionItem) -> Unit
) : RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder>() {

    class NotificacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcono: ImageView = itemView.findViewById(R.id.ivBackNotificaciones)
        val tvMensaje: TextView = itemView.findViewById(R.id.tvMensajeNotificacion)
        val tvTiempo: TextView = itemView.findViewById(R.id.tvTiempoNotificacion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificacionViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notificacion, parent, false)
        return NotificacionViewHolder(vista)
    }

    override fun onBindViewHolder(holder: NotificacionViewHolder, position: Int) {
        val item = notificaciones[position]
        holder.tvMensaje.text = item.mensaje
        holder.tvTiempo.text = item.tiempo

        holder.itemView.setOnClickListener {
            onNotificacionClick(item)
        }
    }

    override fun getItemCount(): Int = notificaciones.size

    fun actualizarLista(nuevaLista: List<NotificacionItem>) {
        notificaciones = nuevaLista
        notifyDataSetChanged()
    }
}