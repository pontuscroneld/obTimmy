package com.example.uppgift21janrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    var myadapter = MyAdapter()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var myRec = findViewById<RecyclerView>(R.id.theRecView)

        myRec.layoutManager = LinearLayoutManager(this)
        myRec.adapter = myadapter

    }
}