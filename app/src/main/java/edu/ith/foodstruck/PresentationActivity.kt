package edu.ith.foodstruck

import FoodTruck
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class PresentationActivity : AppCompatActivity() {
    lateinit var ivFoodtruck: ImageView
    lateinit var tvPresentationTitle: TextView
    lateinit var tvPresentationBread: TextView
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation)
        ivFoodtruck = findViewById(R.id.ivFoodtruck)
        tvPresentationTitle = findViewById(R.id.tvPresentationTitle)
        tvPresentationBread = findViewById(R.id.tvPresentationBread)
        db = Firebase.firestore


        val foodTruckDocumentId = intent.getStringExtra("FoodTruckID")

        if (foodTruckDocumentId != null) {
            db.collection("FoodTruck")
                .document(foodTruckDocumentId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val truck = documentSnapshot.toObject<FoodTruck>()

                    if (truck != null) {

                        tvPresentationTitle.text = truck.companyName
                        tvPresentationBread.text = truck.info
                        Glide.with(this).load(truck.userPicID).into(ivFoodtruck)
                    }


                }
        }
    }
}