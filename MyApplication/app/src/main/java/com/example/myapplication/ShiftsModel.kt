package com.example.myapplication

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat

class ShiftsModel(app: Application) : AndroidViewModel(app), CoroutineScope by MainScope() {

    val database = DatabaseModel(app)
    private var VMdateInfo = MutableLiveData<String>()

    private val errorMessage = MutableLiveData<String>()

    var hourlyWage = 0

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

    fun getDateInfo(): LiveData<String> {
        return VMdateInfo
    }


    fun loadDate(chosenDate: Long, endDate: Long) {
        launch {
            Log.d("timmydebug", "Running fun loadDate in ShiftsModel")

            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
            val dateString = simpleDateFormat.format(chosenDate)

            val firstDate = loadapi(dateString)

            if (firstDate.datum == null) {
                //errorString.value = "Felaktig start"
                return@launch
            }

            var dateText = ""

            dateText += firstDate.datum
            dateText += "\n"
            dateText += firstDate.veckodag

            if(firstDate.rodDag == "Nej"){
                dateText += "\n"
                dateText += "Inte röd dag"
            } else {
                dateText += "\n"
                dateText += "Röd dag"
            }

            if(firstDate.helgdag == null){
                dateText += "\n"
                dateText += "Inte helgdag"
            } else {
                dateText += "\n"
                dateText += firstDate.helgdag
            }

            Log.d("timmydebug", dateText)
            Log.d("timmydebug", chosenDate.toString() + endDate.toString())
            VMdateInfo.value = dateText

            createShift(
                chosenDate,
                endDate,
                firstDate.datum,
                firstDate.veckodag,
                firstDate.rodDag,
                firstDate.helgdag
            )


        }
    }

    fun getErrormessage(): LiveData<String> {
        return errorMessage
    }

    private suspend fun loadapi(dateString: String): apiDateInfo {

        Log.d("timmydebug", "Running fun loadapi in ShiftsModel")

        return withContext(Dispatchers.IO) {
            val theurl = URL("http://sholiday.faboul.se/dagar/v2.1/" + dateString)


            val theConnection = (theurl.openConnection() as? HttpURLConnection)!!.apply {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                setRequestProperty("Accept", "application/json")
            }

            val reader = BufferedReader(theConnection.inputStream.reader())
            val theResultString = reader.readText()
            val theInfo = Gson().fromJson(theResultString, apiDays::class.java)

            return@withContext theInfo.dagar[0]


        }
    }


    fun calcDuration(startStamp: Long, endStamp: Long) {

        Log.d("timmydebug", "Running fun calcDuration in ShiftsModel")
        if (startStamp > endStamp) {

            errorMessage.value = "Slutdatum är före startdatum."

            Log.d("timmydebug", "Slutdatum är före startdatum.")
        } else {

            var diffTime = (endStamp - startStamp) / 1000

            // Diff time är tiden man jobbar i sekunder

            var diffTimeInMinutes = diffTime / 60
            val diffTimeInHours = diffTime / 3600
            val minutesMinusHours = diffTimeInMinutes - (diffTimeInHours * 60)

            var minuteWage = hourlyWage.toDouble() / 60
            var earnings = (diffTimeInHours * hourlyWage) + (minutesMinusHours * minuteWage)

            loadDate(startStamp, endStamp)

        }
    }

    fun createShift(
        startTime: Long,
        endTime: Long,
        date: String,
        dayOfTheWeek: String,
        redDay: String,
        holiday: String?
    ) {

        var newShift = DatabaseModel.SingleShift2()

        if (redDay == "Ja") {
            // Daytype är Holiday
            newShift.weekday = dayType.holidayDay
        }

        if (holiday == null && redDay == "Nej") {

            if (dayOfTheWeek == "Lördag") {
                newShift.weekday = dayType.holidayEve
                // Daytype är holidayEve
            } else {
                newShift.weekday = dayType.notHoliday
                // Daytype är notHoliday
            }
        }

        if (holiday != null && redDay == "Nej") {
            //Daytype är holidayEve
            newShift.weekday = dayType.holidayEve
        }


        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
        val dateString = simpleDateFormat.format(startTime)


        launch(Dispatchers.IO) {
            newShift.shiftDuration = (endTime - startTime) / 1000 / 60
            newShift.startTime = startTime
            newShift.endTime = endTime
            newShift.dayOfTheWeek = dayOfTheWeek
            newShift.shiftEarnings = newShift.getShiftEarnings(hourlyWage.toDouble())
            newShift.obEarnings = newShift.getOBHoursHandels(hourlyWage.toDouble())
            newShift.date = dateString
            newShift.readableTime = newShift.getReadableTimePeriod(
                startMonth,
                startDay,
                startHour,
                startMinute,
                endHour,
                endMinute
            )

            database.shiftDB.ShiftDao().insertAll(newShift)
            Log.d("timmydebug", newShift.toString())
        }



    }

    suspend fun calculateSumOfEarnings(): Double {

        var allShiftEarnings = 0.0
        var allObEarnings = 0.0
        var totalEarnings = 0.0

        return withContext(Dispatchers.IO) {

            var listOfShifts = database.shiftDB.ShiftDao().loadAll()

            for (shift in listOfShifts) {
                Log.d("10Marchdebug", shift.readableTime!!)
                Log.d("10Marchdebug", "This shift is worth: " + shift.shiftEarnings.toString())
                totalEarnings = totalEarnings + shift.shiftEarnings!!
                Log.d("10Marchdebug", "Total earnings are: " + totalEarnings.toString())
            }

            return@withContext totalEarnings

        }
    }
}

class Factory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShiftsModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShiftsModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}