package com.example.finalproject.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.HomeItem

class HomeAdapter(private var itemList: List<HomeItem> , private val clickListener: OnItemClickListener) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: HomeItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val titleTextView: TextView = itemView.findViewById(R.id.item_title)
        val usernameTextView: TextView = itemView.findViewById(R.id.item_username)
        val contentsTextView: TextView = itemView.findViewById(R.id.item_price)
        val salseTextView: TextView = itemView.findViewById(R.id.item_sales_status)

        init {
            itemView.setOnClickListener(this)
        }

        // onBindViewHolder 등의 코드 생략

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedItem = itemList[position]
                clickListener.onItemClick(clickedItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }
    fun updateItemList(newItemList: List<HomeItem>) {
        itemList = newItemList
        notifyDataSetChanged() // Notify the adapter that the dataset has changed
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.titleTextView.text = currentItem.title
        holder.usernameTextView.text = currentItem.email
        holder.contentsTextView.text = currentItem.price.toString()+"원"
        if(currentItem.sales_status)
            holder.salseTextView.text = "판매 완료"
        else
            holder.salseTextView.text = "판매 중"

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}

