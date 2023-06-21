package edu.ith.foodstruck

import FoodTruck
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(
    val context: Context,
    private var clickListener: FoodTruckListActivity,
    private var FoodTruckList: ArrayList<FoodTruck>): RecyclerView
                                            .Adapter<MyAdapter.ViewHolder>() {

    fun filterList(foodTruckList: ArrayList<FoodTruck>){
        FoodTruckList = foodTruckList
    }

    private val layoutInflater : LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= layoutInflater.inflate(R.layout.foodtruck_items,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodTruck = FoodTruckList[position]
        holder.itemTitle.text=foodTruck.companyName
        holder.itemInfoText.text=foodTruck.info

        Glide.with(context).load(foodTruck.userPicID).into(holder.itemImage)

        holder.itemView.setOnClickListener{
            clickListener.clickedItem(foodTruck)
        }
    }
    override fun getItemCount(): Int {
        return FoodTruckList.size

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var itemImage = itemView.findViewById<ImageView>(R.id.itemImage)
            var itemTitle = itemView.findViewById<TextView>(R.id.itemTitle)
            var itemInfoText = itemView.findViewById<TextView>(R.id.itemInfoText)

    }

}


