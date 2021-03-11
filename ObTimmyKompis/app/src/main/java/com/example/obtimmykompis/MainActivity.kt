package com.example.androidv1quiz

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    lateinit var vm : DateViewModel

    var isFirstTime = true

    var dateText = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vm = ViewModelProvider(this).get(DateViewModel::class.java)

        var timebutton = findViewById<Button>(R.id.timeButton)
        timebutton.setOnClickListener {

            vm.getTimeDateCalender()

            var startDP = DatePickerDialog(this)
            startDP.setOnDateSetListener { view, year, month, dayOfMonth ->
                vm.setStartDate(dayOfMonth, month, year)

                var startTD = TimePickerDialog(this, this, vm.hour, vm.minute, true)
                startTD.show()
            }
            startDP.show()
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        if(isFirstTime){
            vm.setStartTime(minute, hourOfDay)
            isFirstTime = false
            pickEndTime()
        } else {
            dateText = vm.setEndTime(minute, hourOfDay).toString()
            isFirstTime = true
            updateUI()
        }

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        TODO("Not yet implemented")
    }

    fun pickEndTime(){
        var endTD = TimePickerDialog(this, this, vm.hour, vm.minute, true)
        endTD.show()
    }

    fun updateUI(){
        findViewById<TextView>(R.id.timeTextview).text = dateText

    }
}