package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController


class WageFragment : Fragment() {

    lateinit var shiftsModel : ShiftsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        shiftsModel = ViewModelProvider(this).get(ShiftsModel::class.java)

        val wageET = view.findViewById<EditText>(R.id.wageTextEdit)

        val goButton = view.findViewById<Button>(R.id.wageGoButton)

        goButton.setOnClickListener {

            if(wageET.text.toString() == "")
            {

            } else {
                var hourlyWage = wageET.text.toString().toInt()

                shiftsModel.hourlyWage = hourlyWage

                hourlyWage?.let{
                    findNavController().navigate(R.id.action_wageToShifts)
                }

            }


        }

    }

}