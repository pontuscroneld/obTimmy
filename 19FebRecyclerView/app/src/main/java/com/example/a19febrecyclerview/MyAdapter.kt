package com.example.a19febrecyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MyAdapter() : RecyclerView.Adapter<MyViewHolder>() {

    var fruits = mutableListOf<String>("Apelsin", "Banan", "Citron")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val vh = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.my_layout_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        return fruits.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.theText.text = fruits[position]

        holder.itemView.setOnClickListener {
            Log.d("PIA9", "Clicked on line nr " + position.toString())

            fruits.remove(position)
            notifyDataSetChanged()
        }
    }

}

class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    var theText = view.findViewById<TextView>(R.id.fancyTextView)

}