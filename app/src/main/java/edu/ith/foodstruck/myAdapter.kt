package edu.ith.foodstruck

import FoodTruck
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class myAdapter (val context: Context,
    private var FoodTruckList: ArrayList<FoodTruck>): RecyclerView
                                            .Adapter<myAdapter.ViewHolder>() {

    val layoutInflater : LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= layoutInflater.inflate(R.layout.foodtruck_items,parent,false)
       //val v = LayoutInflater.from(parent.context).inflate(R.layout.foodtruck_items,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodtruck = FoodTruckList[position]
        holder.itemTitle.text=foodtruck.companyName
        holder.itemInfoText.text=foodtruck.info


        Glide.with(context).load(foodtruck?.userPicID).into(holder.itemImage)
    }

    override fun getItemCount(): Int {
        return FoodTruckList.size


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var itemImage = itemView.findViewById<ImageView>(R.id.itemImage)
            var itemTitle = itemView.findViewById<TextView>(R.id.itemTitle)
            var itemInfoText = itemView.findViewById<TextView>(R.id.itemInfoText)



        init {
            //skriva funktioner, clicklisteners?
        }


    }
}


