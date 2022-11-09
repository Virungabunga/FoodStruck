package edu.ith.foodstruck

import FoodTruck
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class FoodtruckListActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var recyclerView: RecyclerView
     private  var FoodTruckList = ArrayList<FoodTruck>()
    private var filteredList = ArrayList<FoodTruck>()
    private var adapter: myAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foodtruck_list)
        db =Firebase.firestore
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)




        adapter = myAdapter(this,FoodTruckList )
        recyclerView.adapter = adapter

        adapter?.setOnItemClickListener(object : myAdapter.onItemClickListener {


            override fun onItemClick(position: Int) {
                Toast.makeText(
                    this@FoodtruckListActivity,
                    "You clicket on item $position",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@FoodtruckListActivity, PresentationActivity::class.java)
                intent.putExtra("NAME", FoodTruckList[position].companyName)
                intent.putExtra("INFO", FoodTruckList[position].info)
                intent.putExtra("IMAGE", FoodTruckList[position].userPicID)
                startActivity(intent)


            }

        })



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

            override fun onQueryTextChange(newText: String?): Boolean {
             //  FoodTruckList.clear()

                Log.d("!!!", "typing")
                filteredList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    FoodTruckList.forEach{
                        if(it.companyName?.toLowerCase(Locale.getDefault())!!.contains(searchText)||it.info?.toLowerCase(
                                Locale.getDefault())!!.contains(searchText)){
                            filteredList.add(it)
                            Log.d("!!!", "${it.companyName}")

                        }

                    }

                    if (adapter == null) {
                        Log.d("!!!","adapter null")
                    }
                    adapter?.filterList(filteredList)


                }else{

                   // filteredList.addAll(FoodTruckList)
                    adapter?.filterList(FoodTruckList)

                }
                adapter?.notifyDataSetChanged()
                return false
            }

        })


        return super.onCreateOptionsMenu(menu)
    }

}