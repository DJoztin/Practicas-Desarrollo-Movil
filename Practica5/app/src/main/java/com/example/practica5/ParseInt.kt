package com.example.practica5

import android.app.Application
import com.parse.Parse

class ParseInt: Application() {
    override fun onCreate() {
        super.onCreate()
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("p7JJHxjcJGDipquIUKobgA3d66KvQsCH5qmTzsoG")
                .clientKey("0fKjIWzexx2kPsTscGqDyPYTmFn73Gnw7EdIwpBE")
                .server("https://parseapi.back4app.com/")
                .build()
        )
    }
}