package com.example.myapplication

import java.util.*

enum class dayType {
    notHoliday, holidayEve, holidayDay
}


data class SingleShift(var weekday: dayType,
                       var startTime: Long,
                       var endTime: Long,
                       var shiftDuration: Long,
                       var shiftEarnings: Double,
                       var obEarnings: Double,
                       var dayOfTheWeek: String

){
    fun getOBHoursHandels(startTime: Long, endTime: Long) : Double {

        var wage = 100
        var minuteWage = wage.toDouble()/60

        var shift = SingleShift(dayType.holidayDay, 1201239, 12031093, 132131,
                13.0, 0.0, "Måndag")

        val cal = Calendar.getInstance()
        cal.time.time = startTime
        val currentDay = cal[Calendar.DAY_OF_MONTH]


        if (shift.weekday == dayType.holidayDay) {
            var extraWage = shift.shiftEarnings

            return extraWage
            // På en söndag/helgdag så räknas varje timme dubbelt. Därför blir extra wage samma som shiftEarnings.

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

            var extraWage = (OBHours * wage + OBMinutes * minuteWage).toDouble()

            return extraWage

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
                return 0.0

            } else {
                val OBHours = difference / 3600
                difference = difference - OBHours * 3600
                val OBMinutes = difference / 60

                var extraWage = (OBHours * wage + OBMinutes * minuteWage)/2.toDouble()

                return extraWage
                // En vardag tjänar man 50% extra efter kl 18.15.

            }
        } else {
            // Om inget stämmer in, ge ingen OB, något har blivit fel
            return 0.0
        }

    }

    fun getOBHoursRest(startTime: Long, endTime: Long) : Double

    {

        var wage = 100
        var shift = SingleShift(dayType.holidayDay, 1201239, 12031093, 132131,
                13.0, 0.0, "Måndag")

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
            val OBHalfHours = difference / 1800 + 1
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
                return 0.0

            } else {

                val OBHalfHours = difference / 1800 + 1
                val extraWage = OBHalfHours * 11.75

                return extraWage

                // En helgafton tjänar man 11.75kr för varje påbörjad halvtimme från kl 16
            }
        }
        if(shift.weekday == dayType.holidayDay){

            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, currentDay)
            cal.set(Calendar.HOUR, 6)
            cal.set(Calendar.MINUTE, 0)

            var diffTime = (endTime-startTime)/1000

            // Diff time är tiden man jobbar i sekunder

            val workingHalfHours = diffTime / 1800 + 1
            val extraWage = workingHalfHours * 11.75

            return extraWage

            // En helgdag tjänar man 11.75kr för varje påbörjad halvtimme
        }

        return 0.0
    }

}




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


