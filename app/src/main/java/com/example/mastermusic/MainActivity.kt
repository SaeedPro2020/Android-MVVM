package com.example.mastermusic

/* This application is designed by Saeed Farkhondeh.
   The architecture is MVVM.
   In terms of more development, since app would grows and we would have more classes,
   it would be better to transform architecture from MVVM to "Clean architecture" or
   other architectures.
*/

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

    }
}