package edu.ith.foodstruck

import FoodTruck
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class FoodTruckListActivity : AppCompatActivity() , FavoriteAdapter.ClickListener {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private var FoodTruckList = ArrayList<FoodTruck>()
    private var filteredList = ArrayList<FoodTruck>()
    private var adapter: MyAdapter? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foodtruck_list)
        db = Firebase.firestore
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        supportActionBar?.setHomeButtonEnabled(true)

        adapter = MyAdapter( this,this@FoodTruckListActivity,FoodTruckList)
        recyclerView.adapter = adapter

        db.collection("FoodTruck")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot.documents) {
                    val truck = document.toObject<FoodTruck>()
                    if (truck != null)
                        FoodTruckList.add(truck)
                }
                adapter?.notifyDataSetChanged()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_item, menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {

                filteredList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    FoodTruckList.forEach {
                        if (it.companyName?.lowercase(Locale.getDefault())!!
                                .contains(searchText) || it.info?.lowercase(
                                Locale.getDefault()
                            )!!.contains(searchText)
                        ) {
                            filteredList.add(it)
                            Log.d("!!!", "${it.companyName}")

                        }

                    }

                    if (adapter == null) {
                        Log.d("!!!", "adapter null")
                    }
                    adapter?.filterList(filteredList)

                } else {
                    adapter?.filterList(FoodTruckList)

                }
                adapter?.notifyDataSetChanged()
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun clickedItem(foodTruck: FoodTruck) {
       Log.e("!!!","you clicked ${foodTruck.companyName}")

        val intent= Intent(this,PresentationActivity::class.java)
        intent.putExtra("FoodTruckID",foodTruck.documentId)
        startActivity(intent)
    }
}

