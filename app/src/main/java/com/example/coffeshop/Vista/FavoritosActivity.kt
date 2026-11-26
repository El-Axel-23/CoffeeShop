package com.example.coffeshop.Vista

import android.content.Intent
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.graphics.Typeface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.coffeshop.Inicio
import com.example.coffeshop.R
import com.example.coffeshop.Modelo.ProductoFavorito
import com.example.coffeshop.Presentador.FavoritosPresentador

import java.util.Locale

class FavoritosActivity : AppCompatActivity(), FavoritosPresentador.VistaFavoritos, View.OnClickListener, ListaFavoritosAdapter.OnProductoListener {
    private lateinit var presentador: FavoritosPresentador
    private lateinit var rvFavoritos: RecyclerView
    private lateinit var layoutSinFavoritos: View
    private lateinit var txtSinFavoritos: TextView
    private lateinit var txtDescripcionFavoritos: TextView
    private lateinit var btnVerMenu: Button
    private lateinit var btnRegresarFavoritos: View
    private lateinit var adapterFavoritos: ListaFavoritosAdapter
    private lateinit var navInicio: View
    private lateinit var navMenu: View
    private lateinit var navCarrito: View
    private lateinit var navFavoritos: View
    private lateinit var navPerfil: View
    private lateinit var fondoNavInicio: View
    private lateinit var fondoNavMenu: View
    private lateinit var fondoNavCarrito: View
    private lateinit var fondoNavFavoritos: View
    private lateinit var fondoNavPerfil: View
    private lateinit var txtNavInicio: TextView
    private lateinit var txtNavMenu: TextView
    private lateinit var txtNavCarrito: TextView
    private lateinit var txtNavFavoritos: TextView
    private lateinit var txtNavPerfil: TextView
    private var idUsuario = 0

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
        adapterFavoritos = ListaFavoritosAdapter(this)
        rvFavoritos.layoutManager = LinearLayoutManager(this)
        rvFavoritos.adapter = adapterFavoritos
        btnVerMenu.setOnClickListener(this)
        btnRegresarFavoritos.setOnClickListener(this)
        configurarNavegacion()
        seleccionarNavegacion(R.id.navFavoritos)
    }
    private fun configurarNavegacion() {
        navInicio = findViewById(R.id.navInicio)
        navMenu = findViewById(R.id.navMenu)
        navCarrito = findViewById(R.id.navCarrito)
        navFavoritos = findViewById(R.id.navFavoritos)
        navPerfil = findViewById(R.id.navPerfil)
        fondoNavInicio = findViewById(R.id.fondoNavInicio)
        fondoNavMenu = findViewById(R.id.fondoNavMenu)
        fondoNavCarrito = findViewById(R.id.fondoNavCarrito)
        fondoNavFavoritos = findViewById(R.id.fondoNavFavoritos)
        fondoNavPerfil = findViewById(R.id.fondoNavPerfil)
        txtNavInicio = findViewById(R.id.txtNavInicio)
        txtNavMenu = findViewById(R.id.txtNavMenu)
        txtNavCarrito = findViewById(R.id.txtNavCarrito)
        txtNavFavoritos = findViewById(R.id.txtNavFavoritos)
        txtNavPerfil = findViewById(R.id.txtNavPerfil)
        navInicio.setOnClickListener(this)
        navMenu.setOnClickListener(this)
        navCarrito.setOnClickListener(this)
        navFavoritos.setOnClickListener(this)
        navPerfil.setOnClickListener(this)
    }
    // Resaltar el apartado seleccionado
    private fun seleccionarNavegacion(idSeleccionado: Int) {
        fondoNavInicio.isSelected = false
        fondoNavMenu.isSelected = false
        fondoNavCarrito.isSelected = false
        fondoNavFavoritos.isSelected = false
        fondoNavPerfil.isSelected = false
        txtNavInicio.setTypeface(null, Typeface.NORMAL)
        txtNavMenu.setTypeface(null, Typeface.NORMAL)
        txtNavCarrito.setTypeface(null, Typeface.NORMAL)
        txtNavFavoritos.setTypeface(null, Typeface.NORMAL)
        txtNavPerfil.setTypeface(null, Typeface.NORMAL)
        when (idSeleccionado) {
            R.id.navInicio -> {
                fondoNavInicio.isSelected = true
                txtNavInicio.setTypeface(null, Typeface.BOLD)
            }
            R.id.navMenu -> {
                fondoNavMenu.isSelected = true
                txtNavMenu.setTypeface(null, Typeface.BOLD)
            }
            R.id.navCarrito -> {
                fondoNavCarrito.isSelected = true
                txtNavCarrito.setTypeface(null, Typeface.BOLD)
            }
            R.id.navFavoritos -> {
                fondoNavFavoritos.isSelected = true
                txtNavFavoritos.setTypeface(null, Typeface.BOLD)
            }
            R.id.navPerfil -> {
                fondoNavPerfil.isSelected = true
                txtNavPerfil.setTypeface(null, Typeface.BOLD)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        seleccionarNavegacion(R.id.navFavoritos)
        cargarFavoritos()
    }
    // Consultar la sesión del usuario
    private fun cargarFavoritos() {
        val preferencias = getSharedPreferences("CoffeeShopSesion", MODE_PRIVATE)
        val sesionIniciada = preferencias.getBoolean("sesionIniciada", false)
        idUsuario = preferencias.getInt("id_usuario", 0)
        if (!sesionIniciada || idUsuario <= 0) {
            presentador.liberar()
            presentador = FavoritosPresentador(this)
            idUsuario = 0
            adapterFavoritos.actualizarLista(emptyList())
            mostrarEstadoVacio("Inicia sesión para ver tus favoritos", "Accede a tu cuenta para consultar tus bebidas guardadas.")
            return
        }
        presentador.cargarFavoritos(idUsuario)
    }
    // Mostrar los productos recibidos
    override fun mostrarFavoritos(productos: List<ProductoFavorito>) {
        adapterFavoritos.actualizarLista(productos)
        if (productos.isEmpty()) {
            mostrarEstadoVacio("Aún no tienes favoritos", "Toca el corazón en tus bebidas preferidas para guardarlas aquí")
        } else {
            rvFavoritos.visibility = View.VISIBLE
            layoutSinFavoritos.visibility = View.GONE
        }
    }
    private fun mostrarEstadoVacio(titulo: String, descripcion: String) {
        rvFavoritos.visibility = View.GONE
        layoutSinFavoritos.visibility = View.VISIBLE
        txtSinFavoritos.text = titulo
        txtDescripcionFavoritos.text = descripcion
    }
    override fun mostrarCargando(cargando: Boolean) {
        if (cargando) {
            mostrarEstadoVacio("Cargando favoritos...", "Consultando tus bebidas favoritas.")
        }
    }
    override fun mostrarError(mensaje: String) {
        mostrarEstadoVacio("No se pudieron cargar tus favoritos", "Comprueba tu conexión e inténtalo nuevamente.")
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_SHORT
        ).show()
    }
    // Resultado de agregar o eliminar
    override fun mostrarOperacion(id_producto: Int, mensaje: String, es_favorito: Boolean) {
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_SHORT
        ).show()
    }
    // Confirmar la eliminación del producto
    override fun onEliminarProducto(producto: ProductoFavorito) {
        if (idUsuario <= 0) {
            Toast.makeText(
                this,
                "Debes iniciar sesión.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val dialogo = AlertDialog.Builder(this)
        dialogo.setTitle("Eliminar favorito")
        dialogo.setMessage("¿Deseas quitar ${producto.nombre} de tus favoritos?")
        dialogo.setPositiveButton("Eliminar", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    presentador.eliminarFavorito(idUsuario, producto.id_producto)
                }
            }
        )
        dialogo.setNegativeButton("Cancelar", null)
        dialogo.show()
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnRegresarFavoritos -> {
                finish()
            }
            R.id.btnVerMenu -> {
                abrirInicio()
            }
            R.id.navInicio -> {
                abrirInicio()
            }
            R.id.navMenu -> {
                abrirPantalla("com.example.coffeshop.Vista.MenuActivity")
            }
            R.id.navCarrito -> {
                abrirPantalla("com.example.coffeshop.Vista.CarritoActivity")
            }
            R.id.navFavoritos -> {
                seleccionarNavegacion(R.id.navFavoritos)
            }
            R.id.navPerfil -> {
                abrirPantalla("com.example.coffeshop.Vista.PerfilActivity")
            }
        }
    }
    private fun abrirInicio() {
        val intent = Intent(this, Inicio::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }
    private fun abrirPantalla(nombreClase: String) {
        try {
            val intent = Intent()
            intent.setClassName(this, nombreClase)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Esta pantalla todavía no está disponible.", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {
        if (::presentador.isInitialized) {
            presentador.liberar()
        }
        super.onDestroy()
    }
}
class ListaFavoritosAdapter(private val listener: OnProductoListener) : RecyclerView.Adapter<ListaFavoritosAdapter.Holder>() {
    private val lista = ArrayList<ProductoFavorito>()
    interface OnProductoListener {
        fun onEliminarProducto(producto: ProductoFavorito)
    }
    // Actualizar los productos del RecyclerView
    fun actualizarLista(productos: List<ProductoFavorito>) {
        lista.clear()
        lista.addAll(productos)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val vista = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return Holder(vista)
    }
    // Mostrar nombre y precio
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val producto = lista[position]
        holder.txtNombre.text = producto.nombre
        holder.txtPrecio.text = String.format(Locale.forLanguageTag("es-MX"), "$%.2f", producto.precio)
        holder.txtNombre.setTextColor(Color.parseColor("#422018"))
        holder.txtPrecio.setTextColor(Color.parseColor("#795647"))
        // Mantener presionado para eliminar
        holder.itemView.setOnLongClickListener(
            object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {
                    val posicion = holder.bindingAdapterPosition
                    if (posicion != RecyclerView.NO_POSITION) {
                        listener.onEliminarProducto(lista[posicion])
                        return true
                    }
                    return false
                }
            }
        )
    }
    override fun getItemCount(): Int {
        return lista.size
    }
    class Holder(vista: View) : RecyclerView.ViewHolder(vista) {
        val txtNombre: TextView = vista.findViewById(android.R.id.text1)
        val txtPrecio: TextView = vista.findViewById(android.R.id.text2)
    }
}