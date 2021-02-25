package com.example.myapplication

import java.util.*

enum class dayType {
    notHoliday, holidayEve, holidayDay
}


data class SingleShift(var weekday: dayType,
                       var startTime: Long,
                       var endTime: Long,
                       var shiftDuration: Long,
                       var id: Int
)

    fun getOBHoursHandels(startTime: Long, endTime: Long) : Long {

        var wage = 100
        var shift = SingleShift(dayType.holidayDay, 1201239, 12031093, 132131, 1)


        val cal = Calendar.getInstance()
        cal.time.time = startTime
        val currentDay = cal[Calendar.DAY_OF_MONTH]


        if (shift.weekday == dayType.holidayDay) {
            shift.shiftDuration *= 2

            return shift.shiftDuration
            // På en söndag/helgdag så räknas varje timme dubbelt. Därför blir Duration dubbel.

        }

            // Holiday Eve
         if (shift.weekday == dayType.holidayEve) {

            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, currentDay)
            cal.set(Calendar.HOUR, 12)
            cal.set(Calendar.MINUTE, 0)

            val OBtimeStamp = cal.time.time
            var difference = (shift.endTime-OBtimeStamp) / 1000

            val OBHours = difference / 3600
            difference = difference - OBHours * 3600
            val OBMinutes = difference / 60

             shift.shiftDuration += OBHours
             shift.shiftDuration += OBMinutes

             return shift.shiftDuration

             // På en lördag ska man få dubbel lön efter klockan tolv. Därför räknas timmar och minuter efter kl 12 två gånger.

        }


        // Vardag
        if(shift.weekday == dayType.notHoliday){
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, currentDay)
            cal.set(Calendar.HOUR, 18)
            cal.set(Calendar.MINUTE, 15)

            val OBtimeStamp = cal.time.time
            var difference = (shift.endTime-OBtimeStamp) / 1000

            if(difference < 0){
                // PERSONEN FÅR INGEN OB

            } else {
                val OBHours = difference / 3600
                difference = difference - OBHours * 3600
                val OBMinutes = difference / 60

                shift.shiftDuration += OBHours/2
                shift.shiftDuration += OBMinutes/2

                return shift.shiftDuration
                // En vardag tjänar man 50% extra efter kl 18.15. 

            }
        }

        return shift.shiftDuration
    }

fun getOBHoursRest(startTime: Long, endTime: Long) : Double

{

    var wage = 100
    var shift = SingleShift(dayType.holidayDay, 1201239, 12031093, 132131, 1)

    val cal = Calendar.getInstance()
    cal.time.time = startTime
    val currentDay = cal[Calendar.DAY_OF_MONTH]


    if(shift.weekday == dayType.notHoliday){
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, currentDay)
        cal.set(Calendar.HOUR, 20)
        cal.set(Calendar.MINUTE, 0)

        val OBtimeStamp = cal.time.time
        var difference = (shift.endTime-OBtimeStamp) / 1000

        if(difference < 0){
            // PERSONEN FÅR INGEN OB

        } else {

        }
            val OBHalfHours = difference / 7200
            val extraWage = OBHalfHours * 11.75

            return extraWage

            // En vardag tjänar man 11.75kr för varje påbörjad halvtimme
    }
    if(shift.weekday == dayType.holidayEve){

        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, currentDay)
        cal.set(Calendar.HOUR, 16)
        cal.set(Calendar.MINUTE, 0)

        val OBtimeStamp = cal.time.time
        var difference = (shift.endTime-OBtimeStamp) / 1000

        if(difference < 0){
            // PERSONEN FÅR INGEN OB

        } else {

            val OBHalfHours = difference / 7200
            val extraWage = OBHalfHours * 11.75

            return extraWage

            // En helgafton tjänar man 11.75kr för varje påbörjad halvtimme
        }
    }
    if(shift.weekday == dayType.holidayDay){

        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, currentDay)
        cal.set(Calendar.HOUR, 6)
        cal.set(Calendar.MINUTE, 0)

        val OBtimeStamp = cal.time.time
        var difference = (shift.endTime-OBtimeStamp) / 1000

        if(difference < 0){
            // PERSONEN FÅR INGEN OB

        } else {

        }
        val OBHalfHours = difference / 7200
        val extraWage = OBHalfHours * 11.75

        return extraWage

        // En helgdag tjänar man 11.75kr för varje påbörjad halvtimme
    }
 else{
     return 0.0
    }

    return 0.0
}
/*
fun calcDuration()
    {
        val cal = Calendar.getInstance()
        //cal.set(savedHour, savedMinute)
        cal.set(Calendar.HOUR, startHour)
        cal.set(Calendar.MINUTE, startMinute)

        val startStamp = cal.timeInMillis

        cal.set(Calendar.HOUR, endHour)
        cal.set(Calendar.MINUTE, endMinute)

        val endStamp = cal.time.time
        var diffStamp = (endStamp-startStamp)/1000

        // 37.8653

        val workHours = diffStamp/3600
        diffStamp = diffStamp - workHours * 3600
        val workMinutes = diffStamp/60

        // Spara DATUM OCH TID, i shiftobjektet skapa en "getOBfunktion",
        // Skjut på hur jag ska spara lite i framtiden och fundera ut logiken först.

    }
 */

    /*

Måndag–fredag 18.15–20.00      50%
Måndag–fredag efter 20.00      70%
Lördagar efter 12.00           100%
Helgdagar                      100%

     */

/*

23,53 kr

Måndag – fredag från kl. 20.00 till kl. 06.00 påföljande dag.
Lördag, midsommar-, jul- och nyårsafton från kl. 16.00 till kl. 06.00 påföljande dag.
Söndag och helgdag från kl. 06.00 till kl. 06.00 påföljande dag.

 */


