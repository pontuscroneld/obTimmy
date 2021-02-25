package com.example.quiz4feb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = Firebase.database
        val myRef = database.getReference("quizPath")
        val button = findViewById<Button>(R.id.pressButton)
        val textfield = findViewById<EditText>(R.id.firebaseET)


        val firebaseListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val onlineText = dataSnapshot.getValue<String>()

                textfield.setText(onlineText)

            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        myRef.addValueEventListener(firebaseListener)
        button.setOnClickListener {
            // Ladda upp till Firebase

            var textToFirebase = textfield.text.toString()
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("quizPath")

            myRef.setValue(textToFirebase)

        }
    }
}