package se.magictechnology.pia9monroom

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CategoryAdapter: RecyclerView.Adapter<CategoryViewHolder>() {

    var categories : List<ProductDB.Category>? = null

    lateinit var databaseStuff : ProductDB


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val vh = CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {

        categories?.let {
            return it.size
        }

        return 0
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        var currentCategory = categories!![position]
        holder.catnameTextview.text = currentCategory.categoryName

        holder.catDeleteButton.setOnClickListener {


            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Vill du radera?")
            builder.setMessage("Obs farligt!")

            builder.setPositiveButton("Ja") { dialog, which ->
                databaseStuff.deleteCategory(currentCategory)
            }

            builder.setNegativeButton("Nej") { dialog, which ->

            }

            builder.show()

        }
    }

}

class CategoryViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    var catnameTextview = view.findViewById<TextView>(R.id.categoryNameTV)
    var catDeleteButton = view.findViewById<Button>(R.id.categoryDeleteButton)
}