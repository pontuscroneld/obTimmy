package com.example.fragments22jan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FirstFragment : Fragment() {


    var hightlightsAdapter = HightlightsAdapter()
    var categoriesAdapter = CategoriesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hightlightsAdapter.startFragment = this
        categoriesAdapter.startFragment = this

        var highlightsrv = view.findViewById<RecyclerView>(R.id.startHighlightsRV)

        highlightsrv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        highlightsrv.adapter = hightlightsAdapter

        var categoriesrv = view.findViewById<RecyclerView>(R.id.startCategoriesRV)

        categoriesrv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoriesrv.adapter = categoriesAdapter



    }

    fun goRecipe(recipeNumber : Int){

        activity!!.supportFragmentManager.beginTransaction().add(R.id.mainFragment, RecipeDetailFragment()).addToBackStack(null).commit()

    }

    fun goCategory(categoryNumber : Int){

        activity!!.supportFragmentManager.beginTransaction().add(R.id.mainFragment, CategoryFragment()).addToBackStack(null).commit()

    }

}