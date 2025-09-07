package com.example.tarea_calculadoraprom

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.buildIntList
import androidx.collection.buildIntSet
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tarea_calculadoraprom.ui.theme.Tarea_CalculadoraPromTheme

class MainActivity :   AppCompatActivity() {

    private lateinit var rgParcial1: RadioGroup
    private lateinit var rgParcial2: RadioGroup
    private lateinit var rgParcial3: RadioGroup
    private lateinit var rbExcelenteP1: RadioButton
    private lateinit var rbBuenoP1: RadioButton
    private lateinit var rbSuficienteP1: RadioButton
    private lateinit var rbNoPresentadoP1: RadioButton
    private lateinit var rbExcelenteP2: RadioButton
    private lateinit var rbBuenoP2: RadioButton
    private lateinit var rbSuficienteP2: RadioButton
    private lateinit var rbNoPresentadoP2: RadioButton
    private lateinit var rbExcelenteP3: RadioButton
    private lateinit var rbBuenoP3: RadioButton
    private lateinit var rbSuficienteP3: RadioButton
    private lateinit var rbNoPresentadoP3: RadioButton
    private lateinit var cbTareas: CheckBox
    private lateinit var cbParticipacion: CheckBox
    private lateinit var cbProyecto: CheckBox
    private lateinit var cbAsistencia: CheckBox
    private lateinit var btnCalcular: Button
    private lateinit var tvResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        rgParcial1 = findViewById(R.id.rgParcial1)
        rgParcial2 = findViewById(R.id.rgParcial2)
        rgParcial3 = findViewById(R.id.rgParcial3)
        rbExcelenteP1 = findViewById(R.id.rbExelenteP1)
        rbExcelenteP2 = findViewById(R.id.rbExelenteP2)
        rbExcelenteP3 = findViewById(R.id.rbExelenteP3)
        rbBuenoP1 = findViewById(R.id.rbBuenoP1)
        rbBuenoP2 = findViewById(R.id.rbBuenoP2)
        rbBuenoP3 = findViewById(R.id.rbBuenoP3)
        rbSuficienteP1 = findViewById(R.id.rbSuficienteP1)
        rbSuficienteP2 = findViewById(R.id.rbSuficienteP2)
        rbSuficienteP3 = findViewById(R.id.rbSuficienteP3)
        rbNoPresentadoP1 = findViewById(R.id.rbNoPresentadoP1)
        rbNoPresentadoP2 = findViewById(R.id.rbNoPresentadoP2)
        rbNoPresentadoP3 = findViewById(R.id.rbNoPresentadoP3)
        cbTareas = findViewById(R.id.cbTareas)
        cbParticipacion = findViewById(R.id.cbParticipacion)
        cbProyecto = findViewById(R.id.cbProyecto)
        cbAsistencia = findViewById(R.id.cbAsistencia)
        btnCalcular = findViewById(R.id.btnCalcular)
        tvResultado = findViewById(R.id.tvResultado)


        rgParcial1.setOnCheckedChangeListener {
                _,checkedId ->
            val calif1 = when (checkedId){
                R.id.rbExelenteP1 -> 3
                R.id.rbBuenoP1 -> 2
                R.id.rbSuficienteP1 -> 1
                R.id.rbNoPresentadoP1 -> 0

                else-> 0
            }
        }
        rgParcial2.setOnCheckedChangeListener {
                _,checkedId ->
            val calif1 = when (checkedId){
                R.id.rbExelenteP2 -> 3
                R.id.rbBuenoP2 -> 2
                R.id.rbSuficienteP2 -> 1
                R.id.rbNoPresentadoP2 -> 0

                else-> 0
            }
        }

        rgParcial3.setOnCheckedChangeListener {
                _,checkedId ->
            val calif1 = when (checkedId){
                R.id.rbExelenteP3 -> 3
                R.id.rbBuenoP3 -> 2
                R.id.rbSuficienteP3 -> 1
                R.id.rbNoPresentadoP3 -> 0

                else-> 0
            }
        }
        val checkListener = CompoundButton.OnCheckedChangeListener{
                button, isChecked ->
            Log.i("Checkbox", "Checkbox ${button.text}: $isChecked")
        }

        cbTareas.setOnCheckedChangeListener(checkListener)
        cbParticipacion.setOnCheckedChangeListener(checkListener)
        cbProyecto.setOnCheckedChangeListener(checkListener)
        cbAsistencia.setOnCheckedChangeListener(checkListener)
        //Accion
        btnCalcular.setOnClickListener {
            var califP1 = when (rgParcial1.checkedRadioButtonId){
                R.id.rbExelenteP1 -> 3
                R.id.rbBuenoP1 -> 2
                R.id.rbSuficienteP1 -> 1
                R.id.rbNoPresentadoP1 -> 0

                else-> 0
            }
            var califP2 = when (rgParcial2.checkedRadioButtonId){
                R.id.rbExelenteP2  -> 3
                R.id.rbBuenoP2 -> 2
                R.id.rbSuficienteP2 -> 1
                R.id.rbNoPresentadoP2 -> 0

                else-> 0
            }
            var califP3 = when (rgParcial3.checkedRadioButtonId){
                R.id.rbExelenteP3  -> 3
                R.id.rbBuenoP3 -> 2
                R.id.rbSuficienteP3 -> 1
                R.id.rbNoPresentadoP3 -> 0

                else-> 0
            }

            val puntos =  buildList {
                 if (cbTareas.isChecked) add(1)
                if (cbAsistencia.isChecked) add(1)
                if (cbProyecto.isChecked) add(1)
                if (cbParticipacion.isChecked) add(1)

            }.sum()

            val promedio = califP1 + califP2 + califP3 + puntos
            Log.i("Promedio", "Promedio: $promedio")
            val txt = if(promedio >= 7){
                 "Resultado: Aprobado"
            }
            else{
                "Resultado: Requiere extraordinario"

            }
            tvResultado.text = txt
            Log.i("Estatus","$txt")



        }
        restoreState(savedInstanceState)


    }
    fun restoreState(savedInstanceState: Bundle?){

        savedInstanceState?.let {
                state ->
            rgParcial1.check(state.getInt("rgParcial1", -1))
            rgParcial2.check(state.getInt("rgParcial2", -1))
            rgParcial3.check(state.getInt("rgParcial3", -1))
            cbTareas.isChecked = state.getBoolean("cbTareas", false)
            cbProyecto.isChecked = state.getBoolean("cbProyecto", false)
            cbAsistencia.isChecked = state.getBoolean("cbAsistencia", false)
            cbParticipacion.isChecked = state.getBoolean("cbParticipacion", false)
            tvResultado.text = state.getString("tvResultado", "Resultado:")
        }
    }
        override fun onSaveInstanceState(outState: Bundle){
            super.onSaveInstanceState(outState)
            outState.putInt("rgParcial1", rgParcial1.checkedRadioButtonId)
            outState.putInt("rgParcial2", rgParcial2.checkedRadioButtonId)
            outState.putInt("rgParcial3", rgParcial3.checkedRadioButtonId)
            outState.getBoolean("cbTareas", cbTareas.isChecked)
            outState.getBoolean("cbAsistencia", cbAsistencia.isChecked)
            outState.getBoolean("cbParticipacion", cbParticipacion.isChecked)
            outState.getBoolean("cbProyecto", cbProyecto.isChecked)
            outState.getString("tvResultado", tvResultado.text.toString())
        }
}

