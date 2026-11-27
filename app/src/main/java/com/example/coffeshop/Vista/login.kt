
package com.example.coffeshop.Vista

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.coffeshop.R
import com.example.coffeshop.Modelo.UsuarioAutenticacion
import com.example.coffeshop.Presentador.LoginPresentador

class login : AppCompatActivity(), LoginPresentador.VistaLogin, View.OnClickListener {
    private lateinit var txtCorreo: EditText
    private lateinit var txtContrasena: EditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var btnVerContrasena: ImageButton
    private lateinit var txtRegistrate: TextView
    private lateinit var txtOlvidasteContrasena: TextView
    private lateinit var presentador: LoginPresentador
    private var contrasenaVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        presentador = LoginPresentador(this)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtContrasena = findViewById(R.id.txtContrasena)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        btnVerContrasena = findViewById(R.id.btnVerContrasena)
        txtRegistrate = findViewById(R.id.txtRegistrate)
        txtOlvidasteContrasena = findViewById(R.id.txtOlvidasteContrasena)
        contrasenaVisible = false
        txtContrasena.transformationMethod = PasswordTransformationMethod.getInstance()
        btnVerContrasena.setImageResource(R.drawable.ic_ojo_cerrado)
        btnVerContrasena.contentDescription = "Mostrar contraseña"
        txtRegistrate.paintFlags = txtRegistrate.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        txtOlvidasteContrasena.paintFlags = txtOlvidasteContrasena.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        btnIniciarSesion.setOnClickListener(this)
        btnVerContrasena.setOnClickListener(this)
        txtRegistrate.setOnClickListener(this)
        txtOlvidasteContrasena.setOnClickListener(this)

        findViewById<View>(R.id.btnGoogle).setOnClickListener(this)
        findViewById<View>(R.id.btnApple).setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            // INICIAR SESION
            R.id.btnIniciarSesion -> {
                val correo = txtCorreo.text.toString()
                val contrasena = txtContrasena.text.toString()
                presentador.iniciarSesion(
                    correo,
                    contrasena
                )
            }
            // MOSTRAR U OCULTAR CONTRASEÑA
            R.id.btnVerContrasena -> {
                cambiarVisibilidadContrasena()
            }
            // ABRIR CREAR CUENTA
            R.id.txtRegistrate -> {
                val siguiente = Intent(
                    this,
                    crear_cuenta::class.java
                )
                siguiente.putExtra(
                    "desde_login",
                    true
                )
                siguiente.putExtra(
                    "desde_carrito",
                    intent.getBooleanExtra(
                        "desde_carrito",
                        false
                    )
                )
                startActivity(siguiente)
            }
            // ABRIR RECUPERAR CONTRASEÑA
            R.id.txtOlvidasteContrasena -> {
                val siguiente = Intent(
                    this,
                    recuperar_contrasena::class.java
                )
                startActivity(siguiente)
            }
            // GOOGLE
            R.id.btnGoogle -> {
                mostrarMensaje(
                    "Inicio con Google aún no configurado"
                )
            }
            // APPLE
            R.id.btnApple -> {
                mostrarMensaje(
                    "Inicio con Apple aún no configurado"
                )
            }
        }
    }
    // MOSTRAR Y OCULTAR CONTRASEÑA
    private fun cambiarVisibilidadContrasena() {
        contrasenaVisible = !contrasenaVisible
        if (contrasenaVisible) {
            // OJO ABIERTO
            txtContrasena.transformationMethod = HideReturnsTransformationMethod.getInstance()
            btnVerContrasena.setImageResource(R.drawable.ic_ojo)
            btnVerContrasena.contentDescription = "Ocultar contraseña"
        } else {
            // OJO CERRADO
            txtContrasena.transformationMethod = PasswordTransformationMethod.getInstance()
            btnVerContrasena.setImageResource(R.drawable.ic_ojo_cerrado)
            btnVerContrasena.contentDescription = "Mostrar contraseña"
        }
        // CONSERVAR EL CURSOR AL FINAL
        txtContrasena.setSelection(txtContrasena.text.length)
    }
    // ERROR EN CORREO
    override fun mostrarErrorCorreo(mensaje: String) {
        txtCorreo.error = mensaje
        txtCorreo.requestFocus()
    }
    // ERROR EN CONTRASEÑA
    override fun mostrarErrorContrasena(mensaje: String) {
        txtContrasena.error = mensaje
        txtContrasena.requestFocus()
    }
    override fun mostrarMensaje(mensaje: String) {
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }
    // CONTROLAR BOTON DURANTE LA PETICION
    override fun mostrarCargando(cargando: Boolean) {
        btnIniciarSesion.isEnabled = !cargando
        if (cargando) {
            btnIniciarSesion.alpha = 0.6f
        } else {
            btnIniciarSesion.alpha = 1.0f
        }
    }
    // LOGIN CORRECTO
    override fun inicioCorrecto(
        usuario: UsuarioAutenticacion
    ) {
        mostrarMensaje(
            "Bienvenido, ${usuario.nombre_completo}"
        )
        // ESTADO LOCAL PARA LA NAVEGACION
        val preferencias = getSharedPreferences(
            "CoffeeShopSesion",
            MODE_PRIVATE
        )
        preferencias.edit()
            .putBoolean("sesionIniciada", true)
            .putInt("id_usuario", usuario.id_usuario)
            .apply()
        // SI EL USUARIO VENIA DESDE EL CARRITO
        if (intent.getBooleanExtra("desde_carrito", false)) {
            finish()
            return
        }
        // ABRIR INICIO SIN DUPLICAR PANTALLAS
        val siguiente = Intent(
            this,
            Inicio::class.java
        )
        siguiente.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        )
        // ENVIAR DATOS DEL USUARIO
        siguiente.putExtra(
            "id_usuario",
            usuario.id_usuario
        )
        siguiente.putExtra(
            "nombre_completo",
            usuario.nombre_completo
        )
        siguiente.putExtra(
            "correo",
            usuario.correo
        )
        startActivity(siguiente)
        finish()
    }
    override fun onDestroy() {
        if (::presentador.isInitialized) {
            presentador.liberar()
        }
        super.onDestroy()
    }
}