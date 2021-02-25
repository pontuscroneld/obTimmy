package com.example.thurs21janandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    var mainAdapter = MainAdapter()
    var secondAdapter = secondAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mainRV = findViewById<RecyclerView>(R.id.mainRV)
        mainRV.layoutManager = LinearLayoutManager(this)
        mainRV.adapter = mainAdapter

        var secondRV = findViewById<RecyclerView>(R.id.secondRV)
        secondRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        secondRV.adapter = secondAdapter

    }
}