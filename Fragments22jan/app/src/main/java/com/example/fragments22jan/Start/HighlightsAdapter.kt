package com.example.fragments22jan

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HightlightsAdapter() : RecyclerView.Adapter<HighlightViewholder>() {

    lateinit var startFragment : FirstFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightViewholder {
        val vh = HighlightViewholder(LayoutInflater.from(parent.context).inflate(R.layout.highlight_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: HighlightViewholder, position: Int) {

        holder.itemView.setOnClickListener {
            Log.d("PIA9 DEBUG", "Klickat på knappen")

            startFragment.goRecipe(recipeNumber = position)

        }

    }

}

class HighlightViewholder (view: View) : RecyclerView.ViewHolder(view) {



}