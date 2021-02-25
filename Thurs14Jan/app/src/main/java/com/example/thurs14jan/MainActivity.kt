package com.example.thurs14jan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible


class MainActivity : AppCompatActivity() {

    var string1 = "Pick your champion"
    var string2 = ""
    var string3 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var championView = findViewById<ImageView>(R.id.championView)
        var subHeadline = findViewById<TextView>(R.id.subHeadline)
        var headline = findViewById<TextView>(R.id.headline)
        var bodyText = findViewById<TextView>(R.id.bodyText)
        var bottomText = findViewById<TextView>(R.id.bottomText)
        var leftButton = findViewById<Button>(R.id.leftButton)
        var rightButton = findViewById<Button>(R.id.rightButton)
        var centerButton = findViewById<Button>(R.id.centerButton)

        updateText()

        championView.setImageResource(R.drawable.gorilla)
        championView.setVisibility(View.INVISIBLE)

        leftButton.setOnClickListener {
            changeText(true)
        }

        rightButton.setOnClickListener {
            changeText(false)
        }

        centerButton.setOnClickListener {
            startText()
                championView.setImageResource(R.drawable.gorilla)
                championView.visibility = View.INVISIBLE
        }

    }

    fun changeText(whichButton: Boolean){

        if(whichButton == false){
            string1 = "Shovel"
            string2 = "You shovel the gorilla."
            string3 = "You strike the gorilla with your shovel. The gorilla is furious and strangles you."

            updateText()

        } else {
            string1 = "Gorilla"
            string2 = "The gorilla picks up the shovel."
            string3 = "The gorilla is confused. In the moment of hesitation the shovel attacks and pierces the gorilla's aeorta."
            updateText()
        }

    }

    fun updateText(){
        var subHeadline = findViewById<TextView>(R.id.subHeadline)
        var headline = findViewById<TextView>(R.id.headline)
        var bodyText = findViewById<TextView>(R.id.bodyText)
        var bottomText = findViewById<TextView>(R.id.bottomText)

        headline.text = string1
        subHeadline.text = string2
        bodyText.text = string3
        bottomText.text = string1

    }

    fun startText(){

        string1 = "Pick your champion"
        string2 = ""
        string3 = ""

        updateText()

    }

    fun showChampion(gorilla: Boolean){

        var championView = findViewById<ImageView>(R.id.championView)
        if(gorilla){
            championView.setImageResource(R.drawable.gorilla)
            championView.visibility = View.VISIBLE
        } else {
            championView.setImageResource(R.drawable.shovel)
            championView.visibility = View.VISIBLE
        }

    }

}