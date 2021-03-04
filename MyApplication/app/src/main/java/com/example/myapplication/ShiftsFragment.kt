package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import java.util.*


class ShiftsFragment : Fragment(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    /*
   Tutorial för datePicker
   https://www.youtube.com/watch?v=GmmyCOpIutA&ab_channel=AndroidDevelopers
    */

    lateinit var shiftsModel : ShiftsModel


    var startDay = 0
    var startMonth = 0
    var startYear = 0
    var endDay = 0
    var endMonth = 0
    var endYear = 0

    var startHour = 0
    var startMinute = 0
    var endHour = 0
    var endMinute = 0

    var hour = 0
    var minute = 0
    var day = 0
    var month = 0
    var year = 0

    var isStartTime = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shifts_2, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shiftsModel = ViewModelProvider(this).get(ShiftsModel::class.java)
        val startTimeButton = view.findViewById<Button>(R.id.shiftsStartTimeButton)

        var wageSlider = view.findViewById<SeekBar>(R.id.shiftsSliderBar)
        wageSlider.max = 250



        wageSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("timmydebug", progress.toString())
                view.findViewById<TextView>(R.id.shiftsWageTV).text = "Timlön: " + progress
                shiftsModel.hourlyWage = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                Log.d("timmydebug", "Touching bar")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })



        startTimeButton.setOnClickListener{
            isStartTime = true
            getTimeDateCalender()
            var startDP = DatePickerDialog(requireContext())
            startDP.setOnDateSetListener { view, year, month, dayOfMonth ->

                startYear = year
                startMonth = month
                startDay = dayOfMonth
                val cal = Calendar.getInstance()
                //cal.set(savedHour, savedMinute)
                cal.set(Calendar.YEAR, startYear)
                cal.set(Calendar.MONTH, startMonth)
                cal.set(Calendar.DAY_OF_MONTH, startDay)

                var startTD = TimePickerDialog(context, this, hour, minute, true)

                startTD.show()
            }
            startDP.show()

        }

        val endTimeButton = view.findViewById<Button>(R.id.shiftsEndTimeButton)

        endTimeButton.setOnClickListener{
            isStartTime = false
            getTimeDateCalender()
            var startDP = DatePickerDialog(requireContext())
            startDP.setOnDateSetListener { view, year, month, dayOfMonth ->
                endYear = year
                endMonth = month
                endDay = dayOfMonth
                val cal = Calendar.getInstance()
                //cal.set(savedHour, savedMinute)
                cal.set(Calendar.YEAR, endYear)
                cal.set(Calendar.MONTH, endMonth)
                cal.set(Calendar.DAY_OF_MONTH, endDay)
                var startTD = TimePickerDialog(context, this, hour, minute, true)
                startTD.show()
            }
            startDP.show()
        }

    }

    private fun getTimeDateCalender(){

        var cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {



        if(isStartTime){
            startHour = hourOfDay
            startMinute = minute
            val cal = Calendar.getInstance()

            cal.set(Calendar.YEAR, startYear)
            cal.set(Calendar.MONTH, startMonth)
            cal.set(Calendar.DAY_OF_MONTH, startDay)
            cal.set(Calendar.HOUR, startHour)
            cal.set(Calendar.MINUTE, startMinute)
            val startStamp = cal.timeInMillis

            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm")
            val dateString = simpleDateFormat.format(startStamp)

            Log.d("timmydebug", dateString)
            Log.d("timmydebug", "Start time set")



        } else {
            endHour = hourOfDay
            endMinute = minute
            val cal = Calendar.getInstance()
            //cal.set(savedHour, savedMinute)
            cal.set(Calendar.YEAR, endYear)
            cal.set(Calendar.MONTH, endMonth)
            cal.set(Calendar.DAY_OF_MONTH, endDay)
            cal.set(Calendar.HOUR, endHour)
            cal.set(Calendar.MINUTE, endMinute)

            val endStamp = cal.timeInMillis

            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
            val dateString = simpleDateFormat.format(endStamp)

            Log.d("timmydebug", dateString)
            Log.d("timmydebug", "End time set")

            calcDurationInFrag()
        }

    }

    fun calcDurationInFrag()
    {

        Log.d("timmydebug", "Running fun calcDurationInFrag in ShiftsFragment")

        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, startYear)
        cal.set(Calendar.MONTH, startMonth)
        cal.set(Calendar.DAY_OF_MONTH, startDay)
        cal.set(Calendar.HOUR, startHour)
        cal.set(Calendar.MINUTE, startMinute)

        val startStamp = cal.timeInMillis

        cal.set(Calendar.YEAR, endYear)
        cal.set(Calendar.MONTH, endMonth)
        cal.set(Calendar.DAY_OF_MONTH, endDay)
        cal.set(Calendar.HOUR, endHour)
        cal.set(Calendar.MINUTE, endMinute)

        val endStamp = cal.timeInMillis

        shiftsModel.calcDuration(startStamp, endStamp)

        shiftsModel.getDateInfo().observe(this) {
            Log.d("timmydebug", it)
        }


    }






    /*
        shiftsModel = ViewModelProvider(this).get(ShiftsModel::class.java)

        val calculateButton = view.findViewById<Button>(R.id.shiftsCalculateButton)
        calculateButton.setOnClickListener {
            findNavController().navigate(R.id.action_shiftsToFinal)
        }

 */


}