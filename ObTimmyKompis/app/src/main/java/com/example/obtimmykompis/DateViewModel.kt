package com.example.obtimmykompis

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*

class DateViewModel: ViewModel() {

    var hour = 0
    var minute = 0
    var day = 0
    var month = 0
    var year = 0

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

    fun getTimeDateCalender(){

        var cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)

    }

    fun setStartDate(setDayOfMonth: Int, setMonth: Int, setYear : Int){

        startYear = setYear
        startMonth = setMonth
        startDay = setDayOfMonth
        val cal = Calendar.getInstance()
        Log.d("timmydebug", "TZ " + cal.timeZone.toString())
        cal.set(Calendar.YEAR, startYear)
        cal.set(Calendar.MONTH, startMonth)
        cal.set(Calendar.DAY_OF_MONTH, startDay)
    }

    fun setStartTime(setMinute: Int, setHour: Int) {
        startHour = setHour
        startMinute = setMinute
        val cal = Calendar.getInstance()
        Log.d("timmydebug", "TZ " + cal.timeZone.toString())

        cal.set(Calendar.YEAR, startYear)
        cal.set(Calendar.MONTH, startMonth)
        cal.set(Calendar.DAY_OF_MONTH, startDay)
        cal.set(Calendar.HOUR_OF_DAY, startHour)
        cal.set(Calendar.MINUTE, startMinute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val startStamp = cal.timeInMillis

    }

    fun setEndTime(setMinute: Int, setHour: Int) : String {
        endHour = setHour
        endMinute = setMinute
        val cal = Calendar.getInstance()
        Log.d("timmydebug", "TZ " + cal.timeZone.toString())

        cal.set(Calendar.YEAR, startYear)
        cal.set(Calendar.MONTH, startMonth)
        cal.set(Calendar.DAY_OF_MONTH, startDay)
        cal.set(Calendar.HOUR_OF_DAY, endHour)
        cal.set(Calendar.MINUTE, endMinute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val endStamp = cal.timeInMillis

        var readablePeriod = getReadableTimePeriod(startMonth,startDay,startHour,startMinute,endHour,endMinute)

        return readablePeriod
    }

    fun getReadableTimePeriod(sMon : Int,sD : Int, sH : Int, sMin : Int, eH : Int, eMin : Int) : String{

        var sMonString = ""
        var sDayString = ""
        var sHourString = ""
        var sMinString = ""
        var eHourString = ""
        var eMinString = ""

        if(sMon < 10){ sMonString = "0" + sMon.toString() } else{ sMonString = sMon.toString() }

        if(sD < 10){ sDayString = "0" + sD.toString() } else{ sDayString = sD.toString() }

        if(sH < 10){ sHourString = "0" + sH.toString() } else{ sHourString = sH.toString() }

        if(sMin < 10){ sMinString = "0" + sMin.toString() } else{ sMinString = sMin.toString() }

        if(eH < 10){ eHourString = "0" + eH.toString() } else{ eHourString = eH.toString() }

        if(eMin < 10){ eMinString = "0" + eMin.toString() } else{ eMinString = eMin.toString() }

        var readableText = sDayString + "/" + sMonString + " " + sHourString + ":" + sMinString + " - " + eHourString + ":" + eMinString
        return readableText
    }

}