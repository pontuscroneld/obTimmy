package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
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
    lateinit var loadingBar : ProgressBar
    lateinit var sumTV1 : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_final, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingBar = view.findViewById<ProgressBar>(R.id.finalProgressBar)
        resultTV = view.findViewById<TextView>(R.id.finalResultTV)
        resultTV.visibility = View.VISIBLE
        loadingBar.visibility = View.INVISIBLE
        databaseModel = DatabaseModel(requireContext())
        shiftsModel = ViewModelProvider(this).get(ShiftsModel::class.java)
        shiftsModel.database = databaseModel
        sumTV1 = view.findViewById<TextView>(R.id.finalTV1)


        var taxSlider = view.findViewById<SeekBar>(R.id.finalSeekBar)
        taxSlider.max = 50
        taxSlider.progress = sliderValue

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

        view.findViewById<Button>(R.id.finalBackButton).setOnClickListener {
            findNavController().navigate(R.id.actionFinalBack)
        }
    }

    fun applyTaxForReals(taxrate: Double){

        showHide(loadingBar) //Invisible
        showHide(resultTV) // Visible

        launch(Dispatchers.IO) {

            var totalEarnings = shiftsModel.calculateSumOfEarnings()
            var taxRateMultiplication = (100-taxrate)/100
            var finalPayment = totalEarnings*taxRateMultiplication
            var fpi = finalPayment.roundToInt()
            Log.d("12march", shiftsModel.seeTotalTime())
            Log.d("12march", finalPayment.toString())
            var line1 = "Total arbetstid:\n" + shiftsModel.seeTotalTime()
            var line2 = shiftsModel.seeTotalOBEarnings()

            launch(Dispatchers.Main){
                resultTV.text = fpi.toString() + "kr"
                sumTV1.text = line1 + line2
                //sumTV2.text = line2
                showHide(loadingBar) // Visible
                showHide(resultTV)
            }
        }
    }

    fun showHide(view:View) {
        view.visibility = if (view.visibility == View.VISIBLE){
            View.INVISIBLE
        } else{
            View.VISIBLE
        }
    }
}