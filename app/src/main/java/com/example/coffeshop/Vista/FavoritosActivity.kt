package com.example.coffeshop.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.coffeshop.Inicio
import com.example.coffeshop.R
import com.example.coffeshop.Modelo.ProductoFavorito
import com.example.coffeshop.Presentador.FavoritosPresentador

import java.util.Locale

class FavoritosActivity : AppCompatActivity(), FavoritosPresentador.VistaFavoritos, View.OnClickListener {

    private lateinit var presentador: FavoritosPresentador
    private lateinit var rvFavoritos: RecyclerView
    private lateinit var layoutSinFavoritos: View
    private lateinit var txtSinFavoritos: TextView
    private lateinit var txtDescripcionFavoritos: TextView
    private lateinit var btnVerMenu: Button
    private lateinit var btnRegresarFavoritos: View
    private lateinit var adapterFavoritos: ListaFavoritosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favoritos)
        presentador = FavoritosPresentador(this)
        rvFavoritos = findViewById(R.id.rvFavoritos)
        layoutSinFavoritos = findViewById(R.id.layoutSinFavoritos)
        txtSinFavoritos = findViewById(R.id.txtSinFavoritos)
        txtDescripcionFavoritos = findViewById(R.id.txtDescripcionFavoritos)
        btnVerMenu = findViewById(R.id.btnVerMenu)
        btnRegresarFavoritos = findViewById(R.id.btnRegresarFavoritos)
        adapterFavoritos = ListaFavoritosAdapter()
        rvFavoritos.layoutManager = LinearLayoutManager(this)
        rvFavoritos.adapter = adapterFavoritos
        btnVerMenu.setOnClickListener(this)
        btnRegresarFavoritos.setOnClickListener(this)
    }
    override fun onResume() {
        super.onResume()
        cargarFavoritos()
    }
    private fun cargarFavoritos() {
        val preferencias = getSharedPreferences("CoffeeShopSesion", MODE_PRIVATE)
        val sesionIniciada = preferencias.getBoolean("sesionIniciada", false)
        val idUsuario = preferencias.getInt("id_usuario", 0)
        // VERIFICAR SI EL USUARIO INICIO SESION
        if (!sesionIniciada || idUsuario <= 0) { adapterFavoritos.actualizarLista(emptyList())
            mostrarEstadoVacio("Inicia sesión para ver tus favoritos", "Accede a tu cuenta para consultar tus bebidas guardadas.")
            return
        }
        // CONSULTAR FAVORITOS EN EL PRESENTADOR
        presentador.cargarFavoritos(idUsuario)
    }
    // MOSTRAR PRODUCTOS FAVORITOS
    override fun mostrarFavoritos(productos: List<ProductoFavorito>) {
        if (productos.isEmpty()) {
            // NO EXISTEN FAVORITOS
            adapterFavoritos.actualizarLista(emptyList())
            mostrarEstadoVacio("Aún no tienes favoritos", "Toca el corazón en tus bebidas preferidas para guardarlas aquí")
        } else {
            // EXISTEN FAVORITOS
            adapterFavoritos.actualizarLista(productos)
            rvFavoritos.visibility = View.VISIBLE
            layoutSinFavoritos.visibility = View.GONE
        }
    }
    // MOSTRAR PANTALLA SIN FAVORITOS
    private fun mostrarEstadoVacio(titulo: String, descripcion: String) {
        rvFavoritos.visibility = View.GONE
        layoutSinFavoritos.visibility = View.VISIBLE
        txtSinFavoritos.text = titulo
        txtDescripcionFavoritos.text = descripcion
    }
    // MOSTRAR ESTADO DE CARGA
    override fun mostrarCargando(cargando: Boolean) {
        if (cargando) {
            mostrarEstadoVacio("Cargando favoritos...", "Consultando tus bebidas favoritas.")
        }
    }
    // MOSTRAR ERRORES
    override fun mostrarError(mensaje: String) {
        mostrarEstadoVacio("No se pudieron cargar tus favoritos", "Comprueba tu conexión e inténtalo nuevamente.")
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_SHORT
        ).show()
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            // REGRESAR A LA PANTALLA ANTERIOR
            R.id.btnRegresarFavoritos -> {
                finish()
            }
            // ABRIR MENU DE PRODUCTOS
            R.id.btnVerMenu -> {
                val intent = Intent(
                    this,
                    Inicio::class.java
                )
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                )
                startActivity(intent)
            }
        }
    }
    override fun onDestroy() {
        if (::presentador.isInitialized) {
            presentador.liberar()
        }
        super.onDestroy()
    }
}
private class ListaFavoritosAdapter :
    RecyclerView.Adapter<ListaFavoritosAdapter.Holder>() {
    private val lista = ArrayList<ProductoFavorito>()
    // ACTUALIZAR LISTA
    fun actualizarLista(
        productos: List<ProductoFavorito>
    ) {
        lista.clear()
        lista.addAll(productos)
        notifyDataSetChanged()
    }
    // CREAR ELEMENTO DE LA LISTA
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val vista = LayoutInflater.from(
            parent.context
        ).inflate(
            android.R.layout.simple_list_item_2,
            parent,
            false
        )
        return Holder(vista)
    }
    // MOSTRAR DATOS DEL PRODUCTO
    override fun onBindViewHolder(
        holder: Holder,
        position: Int
    ) {
        val producto = lista[position]
        holder.txtNombre.text = producto.nombre
        holder.txtPrecio.text = String.format(
            Locale.forLanguageTag("es-MX"),
            "$%.2f",
            producto.precio
        )
        // COLORES DE COFFEE SHOP
        holder.txtNombre.setTextColor(
            android.graphics.Color.parseColor("#422018")
        )
        holder.txtPrecio.setTextColor(
            android.graphics.Color.parseColor("#795647")
        )
    }
    // TOTAL DE FAVORITOS
    override fun getItemCount(): Int {
        return lista.size
    }
    // VIEWHOLDER
    class Holder(vista: View) :
        RecyclerView.ViewHolder(vista) {
        val txtNombre: TextView = vista.findViewById(
            android.R.id.text1
        )
        val txtPrecio: TextView = vista.findViewById(
            android.R.id.text2
        )
    }
}