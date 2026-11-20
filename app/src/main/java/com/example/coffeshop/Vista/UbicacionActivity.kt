package com.example.coffeshop.Vista

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeshop.R

class UbicacionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubicacion)

        findViewById<android.widget.Button>(R.id.btnVerRuta).setOnClickListener {
            android.widget.Toast.makeText(this, "Cafetería Central, Huejutla de Reyes", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}