package com.example.monday11intro


import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
        var setNumberButton = findViewById<Button>(R.id.setNumberButton)
        var goOtherButton = findViewById<Button>(R.id.goOtherButton)

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
            resetCounting()
        }
        setNumberButton.setOnClickListener {
            var numberEditText = findViewById<EditText>(R.id.numberEditText)
            var enteredText = numberEditText.text.toString()
            Log.d("PIA9DEBUG", enteredText)

/*
            var enteredNumber = enteredText.toIntOrNull()
            if(enteredNumber != null) {
                counterNumber = enteredNumber
            }
            counterTextView.text = counterNumber.toString()
*/

            enteredText.toIntOrNull()?.let{
                counterNumber = it
                counterTextView.text = counterNumber.toString()
            }

            numberEditText.setText("")
        }
        goOtherButton.setOnClickListener {
            Log.d("PIA9DEBUG", "Let's go")

            var intent = Intent(this, OtherActivity::class.java)
            intent.putExtra("counter", counterNumber)
            startActivity(intent)

        }
    }

    fun resetCounting()
    {
        counterNumber = 0
        count = 0
        var counterTextView = findViewById<TextView>(R.id.counterTextView)
        var counterPressed = findViewById<TextView>(R.id.counterPressed)
        counterTextView.text = counterNumber.toString()
        counterPressed.text = count.toString()
    }



}