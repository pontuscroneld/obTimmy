package com.example.monday11intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class OtherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        var theNumber = intent.getIntExtra("counter", 0)
        var otherTextView = findViewById<TextView>(R.id.otherTextView)

        otherTextView.text = theNumber.toString()
    }
}