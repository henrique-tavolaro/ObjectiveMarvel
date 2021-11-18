package com.example.marvelproject.presenter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelproject.R

class PageAdapter(
    val list: List<Int>,
    val context: Context,
    val currentPage: Int) : RecyclerView.Adapter<PageAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.page_adapter,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val view = holder.tvText
        view.text = item.toString()

            if(currentPage.toString() == view.text) {
                view.background = ContextCompat.getDrawable(context, R.drawable.item_circular_color_red)
                view.setTextColor(Color.parseColor("#ffffff"))
            }

            else {
                view.background = ContextCompat.getDrawable(context, R.drawable.item_circular_color_white)
            }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvText : TextView = view.findViewById(R.id.page_adapter)
    }

}



