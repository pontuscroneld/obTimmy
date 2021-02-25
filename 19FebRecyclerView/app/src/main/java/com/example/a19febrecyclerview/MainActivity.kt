package com.example.a19febrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    var theAdapter = MyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var theRV = findViewById<RecyclerView>(R.id.theRecycler)
        //theRV.layoutManager = LinearLayoutManager(this)
        theRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        theRV.adapter = theAdapter

        var addButton = findViewById<Button>(R.id.addButton)


        addButton.setOnClickListener {
            var editText = findViewById<EditText>(R.id.editText)
            var fruitName = editText.text.toString()

            theAdapter.fruits.add(fruitName)
            editText.text = ""

            theAdapter.notifyDataSetChanged()
        }
    }


}