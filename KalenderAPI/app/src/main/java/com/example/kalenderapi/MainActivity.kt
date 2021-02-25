package com.example.kalenderapi

import android.app.DatePickerDialog
import android.icu.text.MessageFormat.format
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.format
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.lang.String.format
import java.text.DateFormat
import java.text.MessageFormat.format
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    lateinit var vm : MainViewModal

    var myDay = 0
    var myMonth = 0
    var myYear = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vm = ViewModelProvider(this).get(MainViewModal::class.java)



        findViewById<Button>(R.id.pickButton).setOnClickListener{
            getTimeDateCalender()
            var myDatePicker = DatePickerDialog(this)
            myDatePicker.setOnDateSetListener { view, year, month, dayOfMonth ->

                myYear = year
                myMonth = month
                myDay = dayOfMonth
                val cal = Calendar.getInstance()
                //cal.set(savedHour, savedMinute)
                cal.set(Calendar.YEAR, myYear)
                cal.set(Calendar.MONTH, myMonth)
                cal.set(Calendar.DAY_OF_MONTH, myDay)

                val dateInMillis = cal.timeInMillis
                vm.loadDate(dateInMillis)
/*
                val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
                val dateString = simpleDateFormat.format(dateInMillis)
                var textView = findViewById<TextView>(R.id.resultTextView)
                textView.text = String.format("Date: %s", dateString)

 */

            }
            myDatePicker.show()
        }

        vm.getDateInfo().observe(this) {
            findViewById<TextView>(R.id.resultTextView).text = it
        }

    }

    private fun getTimeDateCalender(){

        var cal = Calendar.getInstance()
        myDay = cal.get(Calendar.DAY_OF_MONTH)
        myMonth = cal.get(Calendar.MONTH)
        myYear = cal.get(Calendar.YEAR)

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        TODO("Not yet implemented")
    }


}