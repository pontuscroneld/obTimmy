package com.example.thurs21janandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainAdapter() : RecyclerView.Adapter<MainViewHolder>() {

    var computerNames = mutableListOf<String>("Apple", "Atari", "Commodore")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val vh = MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        return computerNames.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        holder.mainTextView.text = computerNames[position]

    }

}

class MainViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    var mainTextView = view.findViewById<TextView>(R.id.mainTextView)

}