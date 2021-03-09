package com.example.myapplication

import android.app.Application
import android.util.Log
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

    var hourlyWage = 1

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

    fun getDateInfo() : LiveData<String>
    {
        return VMdateInfo
    }


    fun loadDate(chosenDate : Long, endDate : Long)
    {
        launch {
            Log.d("timmydebug", "Running fun loadDate in ShiftsModel")

            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
            val dateString = simpleDateFormat.format(chosenDate)

            val firstDate = loadapi(dateString)

            if(firstDate.datum == null)
            {
                //errorString.value = "Felaktig start"
                return@launch
            }
/*
            val toStop = loadapi(toText)
            Log.d("pia9debug", toStop.name)

            if(toStop.id == "")
            {
                errorString.value = "Felaktig slut"
                return@launch
            }
*/

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

            VMdateInfo.value = dateText

            createShift(chosenDate, endDate, firstDate.datum, firstDate.veckodag, firstDate.rodDag, firstDate.helgdag)

            //TODO("HÄR HAR JAG FÅTT ALL INFORMATION JAG BEHÖVER OM SKIFTET INNAN JAG KAN RÄKNA UT OB")

        }
    }



    private suspend fun loadapi(dateString : String) : apiDateInfo {

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

        fun calcDuration(startStamp : Long, endStamp : Long){

            Log.d("timmydebug", "Running fun calcDuration in ShiftsModel")
            if(startStamp > endStamp)
            {

                Log.d("timmydebug", startStamp.toString() + " is bigger than " + endStamp.toString())
                Log.d("timmydebug", "Slutdatum är före startdatum.")
            } else {

                var diffTime = (endStamp-startStamp)/1000

                // Diff time är tiden man jobbar i sekunder

                var diffTimeInMinutes = diffTime / 60
                val diffTimeInHours = diffTime / 3600
                val minutesMinusHours = diffTimeInMinutes - (diffTimeInHours * 60)

                var minuteWage = hourlyWage.toDouble()/60


                var earnings = (diffTimeInHours * hourlyWage) + (minutesMinusHours * minuteWage )
                //Log.d("timmydebug", "Timlön för " + diffTimeInHours + " timmar + " + minutesMinusHours + " minuter blir " + earnings + "kr")

                //TODO("HÄR HAR JAG RÄKNAT UT ANTAL TIMMAR PÅ ETT ARBETSPASS OCH HUR MYCKET MAN TJÄNAR PÅ DET" + "NU BEHÖVER JAG KOPPA DETTA TILL RÄKNA OB FUNKTIONERNA I SINGLE SHIFT")

                loadDate(startStamp, endStamp)

            }
        }

    fun createShift(startTime : Long, endTime : Long, date: String, dayOfTheWeek : String, redDay : String, holiday : String?){

        //TODO("HUR SKA JAG SÄTTA IHOP ALL INFORMATION TILL EN FIL AV DATAKLASSEN SINGLE SHIFT?")

        var newShift = DatabaseModel.SingleShift2()

        Log.d("timmydebug", "Running fun createShift in ShiftsModel")

        if(redDay == "Ja"){
            // Daytype är Holiday
            newShift.weekday = dayType.holidayDay
        }

        if(holiday == null && redDay == "Nej") {

            if(dayOfTheWeek == "Lördag"){
                newShift.weekday = dayType.holidayEve
                // Daytype är holidayEve
            } else {
                newShift.weekday = dayType.notHoliday
                // Daytype är notHoliday
            }
        }

        if(holiday != null && redDay == "Nej"){
            //Daytype är holidayEve
            newShift.weekday = dayType.holidayEve
        }


        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
        val extendedFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
        val dateString = simpleDateFormat.format(startTime)
        var extDateString = extendedFormat.format(startTime)

        Log.d("timmydebug", "Starttime: " + startTime.toString() + " is it the same as " + extDateString )

        extDateString = extendedFormat.format(endTime)
        Log.d("timmydebug", "Starttime: " + endTime.toString() + " is it the same as " + extDateString )

        launch(Dispatchers.IO){
            newShift.shiftDuration = (endTime - startTime) /1000/60
            newShift.startTime = startTime
            newShift.endTime = endTime
            newShift.dayOfTheWeek = dayOfTheWeek
            newShift.shiftEarnings = newShift.getShiftEarnings(hourlyWage.toDouble())
            newShift.obEarnings = newShift.getOBHoursHandels(hourlyWage.toDouble())
            newShift.date = dateString
            newShift.readableTime = newShift.getReadableTimePeriod(startMonth, startDay, startHour,startMinute, endHour, endMinute)


            database.shiftDB.ShiftDao().insertAll(newShift)


            Log.d("timmydebug", "Added to db")

        }



        Log.d("timmydebug", newShift.toString())

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