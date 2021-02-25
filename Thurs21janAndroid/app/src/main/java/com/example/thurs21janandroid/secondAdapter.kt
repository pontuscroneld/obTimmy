package com.example.thurs21janandroid


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class secondAdapter() : RecyclerView.Adapter<SecondViewHolder>() {

    var computerNames = mutableListOf<String>("Apple", "Atari", "Commodore")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondViewHolder {
        val vh = SecondViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.second_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        return computerNames.size
    }

    override fun onBindViewHolder(holder: SecondViewHolder, position: Int) {

        holder.secondTextView.text = computerNames[position]

        holder.itemView.setOnClickListener {
            computerNames.add("Microsoft")
            notifyDataSetChanged()
        }

    }

}

class SecondViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    var secondTextView = view.findViewById<TextView>(R.id.secondTextView)

}