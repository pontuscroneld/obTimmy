package com.example.kalenderapi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat

class MainViewModal : ViewModel(), CoroutineScope by MainScope() {


    private var VMdateInfo = MutableLiveData<String>()

    fun getDateInfo() : LiveData<String>
    {
        return VMdateInfo
    }


    fun loadDate(chosenDate : Long)
    {
        launch {
            Log.d("pia9debug", "Starta hämtning")

            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
            val dateString = simpleDateFormat.format(chosenDate)

            val firstDate = loadapi(dateString)
            Log.d("pia9debug", firstDate.datum)

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
        }
    }



    private suspend fun loadapi(dateString : String) : apiDateInfo
    {
        return withContext(Dispatchers.IO) {
            val theurl = URL("http://sholiday.faboul.se/dagar/v2.1/"+dateString)

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
            /*
            withContext(Dispatchers.Main) {
                Log.d("pia9debug", "RESULTAT")
                Log.d("pia9debug", theResultString)

                for(somestop in theStops.StopLocation)
                {
                    Log.d("pia9debug", somestop.name)
                    findViewById<TextView>(R.id.resultTextView).text = somestop.id
                }
            }
            */

        }

    }

}