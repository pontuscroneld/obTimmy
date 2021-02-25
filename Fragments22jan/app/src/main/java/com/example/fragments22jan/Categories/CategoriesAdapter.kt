package com.example.fragments22jan


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CategoriesAdapter() : RecyclerView.Adapter<CategoryViewholder>() {

    lateinit var startFragment: FirstFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewholder {
        val vh = CategoryViewholder(LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: CategoryViewholder, position: Int) {

            holder.itemView.setOnClickListener {
                startFragment.goCategory(position)
            }

    }

}

class CategoryViewholder (view: View) : RecyclerView.ViewHolder(view) {



}