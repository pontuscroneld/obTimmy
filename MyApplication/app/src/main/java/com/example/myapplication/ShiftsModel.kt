package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShiftsModel : ViewModel() {



    var shift = MutableLiveData<String>()
    var hourlyWage = 0

    fun getShiftInfo() : LiveData<String>
    {
        return shift
    }


    fun addShift(){



    }
}