package edu.ith.foodstruck

import FoodTruck
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.util.*
import kotlin.collections.ArrayList

class FoodtruckListActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var recyclerView: RecyclerView
    private var foodTruckList = ArrayList<FoodTruck>()
    private var tempList= ArrayList<FoodTruck>()


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_foodtruck_list)





            db = FirebaseFirestore.getInstance()
            tempList= arrayListOf<FoodTruck>()
        tempList.addAll(foodTruckList)


            recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


            recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = myAdapter(this, tempList)
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
                tempList.addAll(foodTruckList)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        menuInflater.inflate(R.menu.menu_item,menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView =item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {


               return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempList.clear()
                val searchText=newText?.toLowerCase(Locale.getDefault())

                if(searchText!!.isNotEmpty()){

                    foodTruckList.forEach {
                        if(it.companyName?.toLowerCase()!!.contains(searchText)||it.info?.toLowerCase()!!.contains(searchText)){
                            tempList.add(it)
                        }
                    }

                    recyclerView.adapter!!.notifyDataSetChanged()
                }else{
                    tempList.clear()
                    tempList.addAll(foodTruckList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }


                return false
            }

        })


        return super.onCreateOptionsMenu(menu)
    }

}