package com.example.myapplication

import android.app.AlertDialog
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import android.content.SharedPreferences
import org.w3c.dom.Text


class ShiftsFragment : Fragment(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, CoroutineScope by MainScope() {

    lateinit var databaseModel : DatabaseModel
    lateinit var shiftsModel : ShiftsModel
    lateinit var shiftsadapter : ShiftsAdapter
    lateinit var sharedPreferences: SharedPreferences

    var sliderValue = 0

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

        databaseModel = DatabaseModel(requireContext())
        shiftsModel = ViewModelProvider(this).get(ShiftsModel::class.java)
        shiftsModel.database = databaseModel
        shiftsadapter = ShiftsAdapter(ctx = requireContext())
        shiftsModel.getAllShifts()
        shiftsadapter.shiftFrag = this

        var savedWage = loadWageData()

        if(savedWage != 0){
            view.findViewById<SeekBar>(R.id.shiftsSliderBar).isEnabled = false
        }

        view.findViewById<TextView>(R.id.shiftsWageTV).text = "Timlön: " + savedWage.toInt()


        var shiftRV = view.findViewById<RecyclerView>(R.id.shiftsRecView)
        shiftRV.layoutManager = LinearLayoutManager(context)
        shiftRV.adapter = shiftsadapter


        val startTimeButton = view.findViewById<Button>(R.id.shiftsStartTimeButton)

        var wageSlider = view.findViewById<SeekBar>(R.id.shiftsSliderBar)
        wageSlider.max = 250
        wageSlider.progress = savedWage



        wageSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("timmydebug", progress.toString())
                view.findViewById<TextView>(R.id.shiftsWageTV).text = "Timlön: " + progress
                sliderValue = progress
                shiftsModel.hourlyWage = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                Log.d("timmydebug", "Touching bar")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        val wageSwitch = view.findViewById<Switch>(R.id.shiftsWageSwitch)

        if(wageSwitch.isChecked){
            wageSwitch.text = "Ändra"
        } else {
            wageSwitch.text = "Spara"
        }

        wageSwitch.setOnClickListener {

            if(wageSwitch.isChecked){
                saveWageData()
                shiftsModel.hourlyWage = sliderValue
                wageSlider.isEnabled = false
                wageSwitch.text = "Ändra"

            } else{
                wageSlider.isEnabled = true
                wageSwitch.text = "Spara"
            }

        }
        // START TIME BUTTON
        startTimeButton.setOnClickListener{
            isStartTime = true
            shiftsModel.getTimeDateCalender()
            var startDP = DatePickerDialog(requireContext())
            startDP.setOnDateSetListener { view, year, month, dayOfMonth ->

                shiftsModel.setStartDate(dayOfMonth, month, year)
                /*
                shiftsModel.startYear = year
                shiftsModel.startMonth = month
                shiftsModel.startDay = dayOfMonth
                val cal = Calendar.getInstance()
                Log.d("timmydebug", "TZ " + cal.timeZone.toString())
                cal.set(Calendar.YEAR, shiftsModel.startYear)
                cal.set(Calendar.MONTH, shiftsModel.startMonth)
                cal.set(Calendar.DAY_OF_MONTH, shiftsModel.startDay)
                */

                var startTD = TimePickerDialog(context, this, shiftsModel.hour, shiftsModel.minute, true)

                startTD.show()
            }
            startDP.show()

        }
        // END TIME BUTTON
        val endTimeButton = view.findViewById<Button>(R.id.shiftsEndTimeButton)

        endTimeButton.setOnClickListener{
            isStartTime = false
            shiftsModel.getTimeDateCalender()
            var startDP = DatePickerDialog(requireContext())
            startDP.setOnDateSetListener { view, year, month, dayOfMonth ->
                shiftsModel.endYear = year
                shiftsModel.endMonth = month
                shiftsModel.endDay = dayOfMonth
                val cal = Calendar.getInstance()
                //cal.set(savedHour, savedMinute)
                cal.set(Calendar.YEAR, shiftsModel.endYear)
                cal.set(Calendar.MONTH, shiftsModel.endMonth)
                cal.set(Calendar.DAY_OF_MONTH, shiftsModel.endDay)
                var startTD = TimePickerDialog(context, this, shiftsModel.hour, shiftsModel.minute, true)
                startTD.show()
            }
            startDP.show()
        }

        // UPDATE BUTTON
        val updateButton = view.findViewById<Button>(R.id.shiftUpdateButton)
        updateButton.setOnClickListener {
            shiftsModel.getAllShifts()
        }
        // RESET BUTTON
        val resetButton = view.findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener {

            shiftsModel.deleteAllShifts(requireContext())

        }
        // CALCULATE BUTTON
        val calcButton = view.findViewById<Button>(R.id.shiftsTotalButton)
        calcButton.setOnClickListener {

            var totalText = view.findViewById<TextView>(R.id.shiftsTotalTV)

            launch {
                totalText.text = "Total lön: " + shiftsModel.calculateSumOfEarnings()
            }
        }

        shiftsModel.getErrormessage().observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

        databaseModel.liveDataShiftList.observe(viewLifecycleOwner, { allShifts ->

            shiftsadapter.shiftitems = allShifts
            shiftsadapter.notifyDataSetChanged()
            Log.d("timmydebug", "Data set changed")
        })

        shiftsModel.getAllShifts()

    }

    fun pickEndTime(){
        var endTD = TimePickerDialog(requireContext(), this, shiftsModel.hour, shiftsModel.minute, true)
        endTD.show()
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        if(isStartTime){
            shiftsModel.setStartTime(minute, hourOfDay)
            isStartTime = false
            pickEndTime()
        /*
            shiftsModel.startHour = hourOfDay
            shiftsModel.startMinute = minute
            val cal = Calendar.getInstance()
            Log.d("timmydebug", "TZ " + cal.timeZone.toString())

            cal.set(Calendar.YEAR, shiftsModel.startYear)
            cal.set(Calendar.MONTH, shiftsModel.startMonth)
            cal.set(Calendar.DAY_OF_MONTH, shiftsModel.startDay)
            cal.set(Calendar.HOUR_OF_DAY, shiftsModel.startHour)
            cal.set(Calendar.MINUTE, shiftsModel.startMinute)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val startStamp = cal.timeInMillis

            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm zz")
            val dateString = simpleDateFormat.format(cal.time)

            Log.d("timmydebug", shiftsModel.startHour.toString() + " " + shiftsModel.startMinute.toString())
            Log.d("timmydebug", "Start time set to " + startStamp.toString())
            Log.d("timmydebug", "As a date: " + dateString)
*/
        } else {
            isStartTime = true
            shiftsModel.setEndTime(minute, hourOfDay)



 /*         isFirstTime = true
            shiftsModel.endHour = hourOfDay
            shiftsModel.endMinute = minute
            val cal = Calendar.getInstance()
            //cal.set(savedHour, savedMinute)
            cal.set(Calendar.YEAR, shiftsModel.endYear)
            cal.set(Calendar.MONTH, shiftsModel.endMonth)
            cal.set(Calendar.DAY_OF_MONTH, shiftsModel.endDay)
            cal.set(Calendar.HOUR_OF_DAY, shiftsModel.endHour)
            cal.set(Calendar.MINUTE, shiftsModel.endMinute)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            val endStamp = cal.timeInMillis

            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
            val dateString = simpleDateFormat.format(endStamp)

            Log.d("timmydebug", "End time set to " + endStamp.toString())
            Log.d("timmydebug", "As a date: " + dateString)

            cal.set(Calendar.YEAR, shiftsModel.startYear)
            cal.set(Calendar.MONTH, shiftsModel.startMonth)
            cal.set(Calendar.DAY_OF_MONTH, shiftsModel.startDay)
            cal.set(Calendar.HOUR_OF_DAY, shiftsModel.startHour)
            cal.set(Calendar.MINUTE, shiftsModel.startMinute)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            val startStamp = cal.timeInMillis

            shiftsModel.calcDuration(startStamp, endStamp)
*/
        }
    }

    fun saveWageData(){

        sharedPreferences = this.requireActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply{
            putInt("Wage", sliderValue!!)
            putBoolean("SwitchBool", true)
        }.apply()

        Toast.makeText(this.getActivity(), "Timlön sparad", Toast.LENGTH_SHORT).show()
    }

    fun loadWageData() : Int {

        sharedPreferences = this.requireActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedInt = sharedPreferences.getInt("Wage", 0)
        val savedBoolean = sharedPreferences.getBoolean("SwitchBool", false)

        view?.findViewById<Switch>(R.id.shiftsWageSwitch)!!.isChecked = savedBoolean
        shiftsModel.hourlyWage = savedInt
        return savedInt

    }
}

    /*
        shiftsModel = ViewModelProvider(this).get(ShiftsModel::class.java)

        val calculateButton = view.findViewById<Button>(R.id.shiftsCalculateButton)
        calculateButton.setOnClickListener {
            findNavController().navigate(R.id.action_shiftsToFinal)
        }

 */


