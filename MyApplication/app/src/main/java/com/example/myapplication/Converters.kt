package com.example.myapplication

import androidx.room.TypeConverter
import java.util.*


class Converters {

    @TypeConverter
    fun toDayType(value: String) = enumValueOf<dayType>(value)

    @TypeConverter
    fun fromDayType(value: dayType) = value.name

}