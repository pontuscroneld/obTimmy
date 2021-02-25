package com.example.timepicker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import java.util.*


class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {


    var hour = 0
    var minute = 0
    var savedHour = 0
    var savedMinute = 0

    var listOfDates : List<Long>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pickDateButton = findViewById<Button>(R.id.pickDateButton)

        pickDateButton.setOnClickListener{
            getTimeDateCalender()
            TimePickerDialog(this, this, hour, minute, true).show()
        }
    }

    private fun getTimeDateCalender(){

        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        savedHour = hourOfDay
        savedMinute = minute

        val cal = Calendar.getInstance()
        cal.set(savedHour, savedMinute)
        val time1 = cal.timeInMillis

        saveTime(time1)

    }

    fun saveTime(time : Long){

        var temporaryList : MutableList<Long>? = null
        temporaryList!!.add(time)
        listOfDates = temporaryList

        Log.d("HERE", listOfDates.toString())
    }

}
