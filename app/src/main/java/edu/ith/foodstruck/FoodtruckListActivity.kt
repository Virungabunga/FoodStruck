package edu.ith.foodstruck

import FoodTruck
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class FoodtruckListActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var recyclerView: RecyclerView
    private var foodTruckList = ArrayList<FoodTruck>()


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_foodtruck_list)

            db = FirebaseFirestore.getInstance()

            recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


            recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = myAdapter(this, foodTruckList)
            recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : myAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(this@FoodtruckListActivity,"You clicket on item $position",Toast.LENGTH_SHORT).show()
            val intent = Intent(this@FoodtruckListActivity,PresentationActivity::class.java)
                intent.putExtra("NAME",foodTruckList[position].companyName)
                intent.putExtra("INFO",foodTruckList[position].info)
                intent.putExtra("IMAGE",foodTruckList[position].userPicID)
                startActivity(intent)


            }

        })



            db.collection("FoodTruck")
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    for (document in documentSnapshot.documents) {
                        val truck = document.toObject<FoodTruck>()
                        if (truck != null)
                            foodTruckList.add(truck)


                    }
                    adapter.notifyDataSetChanged()

                }

    }
}