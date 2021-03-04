package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat

class ShiftsModel : ViewModel(), CoroutineScope by MainScope() {

    lateinit var newShift : SingleShift
    private var VMdateInfo = MutableLiveData<String>()

    var hourlyWage = 0

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
            Log.d("timmydebug", firstDate.datum)

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
            Log.d("timmydebug", "Använder createShift-funktionen")
            //createShift(chosenDate, endDate, firstDate.datum, firstDate.veckodag, firstDate.rodDag, firstDate.helgdag)

            //TODO("HÄR HAR JAG FÅTT ALL INFORMATION JAG BEHÖVER OM SKIFTET INNAN JAG KAN RÄKNA UT OB")

        }
    }



    private suspend fun loadapi(dateString : String) : apiDateInfo {

        Log.d("timmydebug", "Running fun loadapi in ShiftsModel")

        return withContext(Dispatchers.IO) {
            val theurl = URL("http://sholiday.faboul.se/dagar/v2.1/" + dateString)

            Log.d("PontusDebug", dateString)
            Log.d("PontusDebug", theurl.toString())

            val theConnection = (theurl.openConnection() as? HttpURLConnection)!!.apply {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                setRequestProperty("Accept", "application/json")
            }

            val reader = BufferedReader(theConnection.inputStream.reader())

            val theResultString = reader.readText()

            Log.d("PontusDebug", theResultString)

            val theInfo = Gson().fromJson(theResultString, apiDays::class.java)

            Log.d("PontusDebug", theInfo.toString())
            return@withContext theInfo.dagar[0]


        }
    }

        fun calcDuration(startStamp : Long, endStamp : Long){

            Log.d("timmydebug", "Running fun calcDuration in ShiftsModel")
            if(startStamp > endStamp)
            {
                Log.d("timmydebug", "Startdatum är före slutdatum.")
            } else {

                var diffTime = (endStamp-startStamp)/1000

                // Diff time är tiden man jobbar i sekunder

                var diffTimeInMinutes = diffTime / 60
                val diffTimeInHours = diffTime / 3600
                val minutesMinusHours = diffTimeInMinutes - (diffTimeInHours * 60)

                Log.d("timmydebug", "Timmar: " + diffTimeInHours + " Minuter: " + minutesMinusHours)

                var minuteWage = hourlyWage.toDouble()/60

                Log.d("timmydebug", "Hourly wage delat på 60 blir: " + minuteWage)

                var earnings = (diffTimeInHours * hourlyWage) + (minutesMinusHours * minuteWage )
                Log.d("timmydebug", "Timlön för " + diffTimeInHours + " timmar + " + minutesMinusHours + " minuter blir " + earnings + "kr")

                //TODO("HÄR HAR JAG RÄKNAT UT ANTAL TIMMAR PÅ ETT ARBETSPASS OCH HUR MYCKET MAN TJÄNAR PÅ DET" + "NU BEHÖVER JAG KOPPA DETTA TILL RÄKNA OB FUNKTIONERNA I SINGLE SHIFT")

                loadDate(startStamp, endStamp)

            }
        }

    fun createShift(startTime : Long, endTime : Long, date: String, dayOfTheWeek : String, redDay : String, holiday : String?){

        //TODO("HUR SKA JAG SÄTTA IHOP ALL INFORMATION TILL EN FIL AV DATAKLASSEN SINGLE SHIFT?")

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







    }



}