package com.example.controlpersonalizado

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sunrise_custom_view = findViewById<CustomView>(R.id.sunrise_custom_view)
        val sunset_custom_view = findViewById<CustomView>(R.id.sunset_custom_view)
       
        sunrise_custom_view.setSubtitle("5:31 AM") // sunrise time set as subtitle
        sunset_custom_view.setSubtitle("5:01 PM") // subset time set as subtitle
    }
}