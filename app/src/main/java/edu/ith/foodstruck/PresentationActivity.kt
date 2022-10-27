package edu.ith.foodstruck

import FoodTruck
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PresentationActivity : AppCompatActivity() {
    lateinit var ivFoodtruck : ImageView
    lateinit var tvPresentationTitle : TextView
    lateinit var tvPresentationBread :TextView
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation)
        ivFoodtruck = findViewById(R.id.ivFoodtruck)
        tvPresentationTitle = findViewById(R.id.tvPresentationTitle)
        tvPresentationBread = findViewById(R.id.tvPresentationBread)
        db =Firebase.firestore
        var foodtruckDocumentId = intent.getStringExtra("FOODTRUCK_ID")!!

        db.collection("FoodTruck")
            .document(foodtruckDocumentId)
            .get()
            .addOnSuccessListener {documentSnapshot ->
                val truck = documentSnapshot.toObject<FoodTruck>()
                tvPresentationTitle.text=truck?.companyName
            }
    }
}