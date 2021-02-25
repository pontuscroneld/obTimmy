package com.example.fragments22jan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.mainFragment, FirstFragment()).commit()


        var secondbutton = findViewById<Button>(R.id.secondButton)

        var isLoggedin = false



        secondbutton.setOnClickListener {

            if(isLoggedin) {
                supportFragmentManager.beginTransaction().replace(R.id.mainFragment, SecondFragment()).addToBackStack(null).commit()
            } else {
                supportFragmentManager.beginTransaction().replace(R.id.mainFragment, LogInFragment()).addToBackStack(null).commit()
            }


        }

        var firstbutton = findViewById<Button>(R.id.firstButton)

        firstbutton.setOnClickListener {

            supportFragmentManager.beginTransaction().replace(R.id.mainFragment, FirstFragment()).addToBackStack(null).commit()
        }

    }


}