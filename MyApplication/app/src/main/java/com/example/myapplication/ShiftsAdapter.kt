package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class ShiftsAdapter(ctx : Context) : RecyclerView.Adapter<MyViewHolder>(), CoroutineScope by MainScope() {

    lateinit var shiftFrag : ShiftsFragment
    var shiftdb = DatabaseModel(ctx)
    var shiftitems : List<DatabaseModel.SingleShift2>? = null

    fun loadShifts()
    {
        launch(Dispatchers.IO) {
            shiftitems = shiftdb.shiftDB.ShiftDao().loadAll()

            launch(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val vh = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.shift_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        shiftitems?.let {
            return it.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var currentItem = shiftitems!![position]

        holder.dateText.text = currentItem.readableTime
        holder.dayOfTheWeekText.text = currentItem.dayOfTheWeek

    }

}

class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    var dayOfTheWeekText = view.findViewById<TextView>(R.id.shiftItemDay)
    var dateText = view.findViewById<TextView>(R.id.shiftItemDate)
}