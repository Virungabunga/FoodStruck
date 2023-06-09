package edu.ith.foodstruck

import FoodTruck
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class FavoritesActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private var foodTruckList = ArrayList<FoodTruck>()
    lateinit var auth:FirebaseAuth
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        db = FirebaseFirestore.getInstance()
        auth=Firebase.auth
        recyclerView = findViewById(R.id.recyclerView)
        supportActionBar?.setHomeButtonEnabled(true)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = FavoriteAdapter(
            this,
            this,
            foodTruckList
        )
        recyclerView.adapter = adapter

        val userId = auth.currentUser!!.uid
        db.collection("users").document(userId).collection("favorites")
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

    fun clickedItem(foodTruck: FoodTruck) {
        val intent= Intent(this,PresentationActivity::class.java)
        intent.putExtra("FoodTruckID",foodTruck.documentId)
        startActivity(intent)
    }
}

