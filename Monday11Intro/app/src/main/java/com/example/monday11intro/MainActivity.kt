package com.example.monday11intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var counterNumber = 0
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var counterTextView = findViewById<TextView>(R.id.counterTextView)
        var counterPressed = findViewById<TextView>(R.id.counterPressed)

        counterTextView.text = counterNumber.toString()
        counterPressed.text = count.toString()

        var plusButton = findViewById<Button>(R.id.plusButton)
        var minusButton = findViewById<Button>(R.id.minusButton)
        var resetButton = findViewById<Button>(R.id.resetButton)

        plusButton.setOnClickListener {
            counterNumber = counterNumber + 1
            count = count + 1
            counterTextView.text = counterNumber.toString()
            counterPressed.text = count.toString()
        }

        minusButton.setOnClickListener {

            if(counterNumber <= 0){
                counterNumber = 0
            } else {
                counterNumber = counterNumber - 1
                count = count + 1
            }
            counterTextView.text = counterNumber.toString()
            counterPressed.text = count.toString()
        }

        resetButton.setOnClickListener {
            counterNumber = 0
            count = 0
            counterTextView.text = counterNumber.toString()
            counterPressed.text = count.toString()
        }
    }

}