package com.example.practica3

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.practica3.databinding.ActivityMainBinding
import kotlinx.serialization.descriptors.serialDescriptor
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var first:Double? = null
    private var op: Char? = null
    private var clearOnNextDigit: Boolean = false

    private var ans: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //funciones lee el texto actual del display

        fun displayText(): String = binding.tvDisplay.text.toString()

        //escribe un texto en el display
        fun setDisplay(s: String){
            binding.tvDisplay.text = s
        }

        //Insterta un digito en el display
        fun appendDigit(d: String){
            if(clearOnNextDigit || displayText() == "0" || displayText() == "Error"){
                setDisplay(d)
                clearOnNextDigit = false

            }else{
                setDisplay(displayText() + d)
            }
        }

        //configura operador y guarda

        fun setOperartor(c: Char){
            first = displayText().toDoubleOrNull()

            op = c

            clearOnNextDigit = true
        }

        //cambia el signo del numero

        fun toggleSing(){
            val v = displayText()
            if (v == "0" || v == "Error") return
            setDisplay(if (v.startsWith("-")) v.drop(1) else "-$v")
        }

        //Agregar punto decimal
        fun addDot(){
            if (displayText()== "Error"){
                setDisplay("0.")
                clearOnNextDigit = false
                return
            }
            if (clearOnNextDigit){
                setDisplay("0.")
                clearOnNextDigit = false
                return
            }

            if (!displayText().contains(".")){
                setDisplay(displayText() + ".")
            }
        }

        //borrar el ultimo caracter

        fun backspace(){
            val v = displayText()
            if (v == "Error" || v.length <= 1 || clearOnNextDigit || (v.length == 2 && v.startsWith("-"))){
                setDisplay("0")
                clearOnNextDigit = false

            }
            else{
                setDisplay(v.dropLast(1))
            }

        }

        //Limpiar todo
        fun clearAll(){
            first = null
            op = null
            clearOnNextDigit = false

            setDisplay("0")
        }

        //operaciones
        fun compute(){
            val a = first ?: return
            val b = displayText().toDoubleOrNull() ?: return

            val res = when(op){
                '+' -> a+b
                '-' -> a-b
                'x' -> a*b
                '/' -> if (abs(b)< 1e-12){
                    setDisplay("Error")
                    first = null; op = null
                    clearOnNextDigit = true
                    return
                }
                else a/b
                else -> return

            }

            ans = res

            setDisplay(if (res % 1.0 == 0.0) res.toLong().toString() else res.toString())

            first = null
            op = null
            clearOnNextDigit= false

        }
    }

}

