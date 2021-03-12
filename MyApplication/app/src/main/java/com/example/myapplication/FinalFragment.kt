package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope


class FinalFragment : Fragment(), CoroutineScope by MainScope() {

    lateinit var databaseModel : DatabaseModel
    lateinit var shiftsModel : ShiftsModel

    var sliderValue = 30

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_final, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseModel = DatabaseModel(requireContext())
        shiftsModel = ViewModelProvider(this).get(ShiftsModel::class.java)
        shiftsModel.database = databaseModel

        var taxSlider = view.findViewById<SeekBar>(R.id.finalSeekBar)
        taxSlider.max = 50
        taxSlider.progress = sliderValue


        taxSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("timmydebug", progress.toString())
                sliderValue = progress
                view.findViewById<TextView>(R.id.finalTaxTV).text = "Skatt: " + sliderValue.toString() + "%"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                Log.d("timmydebug", "Touching bar")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        val resetButton = view.findViewById<Button>(R.id.finalResetButton)
        resetButton.setOnClickListener {

        }



    }

}