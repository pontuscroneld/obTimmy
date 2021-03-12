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
import kotlinx.coroutines.*
import kotlin.math.roundToInt


class FinalFragment : Fragment(), CoroutineScope by MainScope() {

    lateinit var databaseModel : DatabaseModel
    lateinit var shiftsModel : ShiftsModel

    var totalWage = 1000
    var sliderValue = 30
    var sliderDouble = sliderValue.toDouble()

    lateinit var resultTV : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_final, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultTV = view.findViewById<TextView>(R.id.finalResultTV)
        databaseModel = DatabaseModel(requireContext())
        shiftsModel = ViewModelProvider(this).get(ShiftsModel::class.java)
        shiftsModel.database = databaseModel

        var taxSlider = view.findViewById<SeekBar>(R.id.finalSeekBar)
        taxSlider.max = 50
        taxSlider.progress = sliderValue

        //view.findViewById<TextView>(R.id.finalResultTV).text = applyTax(taxrate = sliderDouble, wage = totalWage)
        applyTaxForReals(taxrate = sliderDouble)


        taxSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("timmydebug", progress.toString())
                sliderValue = progress
                sliderDouble = progress.toDouble()
                view.findViewById<TextView>(R.id.finalTaxTV).text = "Skatt: " + sliderValue.toString() + "%"
                //view.findViewById<TextView>(R.id.finalResultTV).text = applyTax(taxrate = sliderDouble, wage = totalWage)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                applyTaxForReals(taxrate = sliderDouble)
            }

        })

        val resetButton = view.findViewById<Button>(R.id.finalResetButton)
        resetButton.setOnClickListener {

            Log.d("timmydebug", "Update")

            //view.findViewById<TextView>(R.id.finalResultTV).text = applyTax(taxrate = sliderDouble, wage = totalWage)
            applyTaxForReals(taxrate = sliderDouble)
        }


    }

    fun applyTax(taxrate: Double, wage : Int) : String {

        var taxRateMultiplication = (100-taxrate)/100

        var finalPayment = wage*taxRateMultiplication
        return finalPayment.toString() + "0 kr"

    }

    fun applyTaxForReals(taxrate: Double){


        launch(Dispatchers.IO) {
            var totalEarnings = shiftsModel.calculateSumOfEarnings()

            var taxRateMultiplication = (100-taxrate)/100

            var finalPayment = totalEarnings*taxRateMultiplication

            var fpi = finalPayment.roundToInt()


            Log.d("12march", finalPayment.toString())



            launch(Dispatchers.Main){
                resultTV.text = fpi.toString() + "kr"
            }

            //showTaxedIncome(finalResult)

        }


    }

    fun showTaxedIncome(String : String){

        resultTV.text = String

    }
}