package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val TAG= "Practica1"
//    private lateinit var binding: ActivityManBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        //Mensaje de arranque
        Log.i(TAG,"La app inicio")

        val etNombre = findViewById<EditText>(R.id.EditText1)
        val btnToast = findViewById<Button >(R.id.btnl1)
        val btnLog = findViewById<Button>(R.id.btnl2)
        val btnLimpiar = findViewById<Button>(R.id.btn1)

        //btnToast listener
        btnToast.setOnClickListener {
            val nombre = etNombre.text?.toString().orEmpty()
            val mensaje = if(nombre.isBlank()) "Hola ISC 7B"
            else "Hola, $nombre"
            Toast.makeText(this,mensaje, Toast.LENGTH_SHORT).show()
        }

        btnLog.setOnClickListener {
            Log.d(TAG, "btnlog: DEBUG")
            Log.i(TAG, "btnlog: INFO")
            Log.w(TAG, "btnlog: WARNING")
            Log.e(TAG, "btnlog: ERROR")
            Log.wtf(TAG,"ola k ase")
            val nombre = etNombre.text?.toString().orEmpty()
            val mensaje = if(nombre.isBlank()) "Hola ISC 7B"
            else "Hola, $nombre"
            Log.i(TAG,mensaje)

        }

        btnLimpiar.setOnClickListener {
            etNombre.text?.clear()
            Toast.makeText(this, "Campo limpio", Toast.LENGTH_LONG).show()
            Log.i(TAG, "btnLimpiar se uso")
        }

    }
}

