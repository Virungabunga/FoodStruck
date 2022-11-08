package edu.ith.foodstruck

import FoodTruck
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log.i
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text


class PresentationActivity : AppCompatActivity() {
    lateinit var ivFoodtruck: ImageView
    lateinit var tvPresentationTitle: TextView
    lateinit var tvPresentationBread: TextView

    lateinit var tvSignatureName : TextView
    lateinit var tvSignatureDescription : TextView
    lateinit var tvSignaturePrice : TextView
    lateinit var ivSignatureImage : ImageView

    lateinit var tvFavoriteName : TextView
    lateinit var tvFavoriteDescription : TextView
    lateinit var tvFavoritePrice : TextView
    lateinit var ivFavoriteImage : ImageView

    lateinit var tvWildName : TextView
    lateinit var tvWildDescription : TextView
    lateinit var tvWildPrice : TextView
    lateinit var ivWildImage : ImageView

    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation)
        ivFoodtruck = findViewById(R.id.ivFoodtruck)
        tvPresentationTitle = findViewById(R.id.tvPresentationTitle)
        tvPresentationBread = findViewById(R.id.tvPresentationBread)
        db = Firebase.firestore
        tvSignatureDescription = findViewById(R.id.tvSignatureDescription)
        tvSignatureName = findViewById(R.id.tvSignatureName)
        tvSignaturePrice = findViewById(R.id.tvSignaturePrice)
        ivSignatureImage = findViewById(R.id.ivSignature)
        tvFavoriteName = findViewById(R.id.tvFavoriteName)
        tvFavoriteDescription = findViewById(R.id.tvFavoriteDescription)
        tvFavoritePrice = findViewById(R.id.tvFavoritePrice)
        ivFavoriteImage =findViewById(R.id.ivFavorite)
        tvWildName = findViewById(R.id.tvWildName)
        tvWildDescription =findViewById(R.id.tvWildDescription)
        tvWildPrice = findViewById(R.id.tvWildPrice)
        ivWildImage = findViewById(R.id.ivWild)
        val rName = intent.getStringExtra("NAME")
        val rInfo =intent.getStringExtra("INFO")
        val rImge = intent.getStringExtra("IMAGE")
        tvPresentationTitle.text=rName
        tvPresentationBread.text=rInfo





        Glide.with(this).load(rImge).into(ivFoodtruck)


        val foodTruckDocumentId = intent.getStringExtra("FoodTruckID")

        if (foodTruckDocumentId != null) {
            db.collection("FoodTruck")
                .document(foodTruckDocumentId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val truck = documentSnapshot.toObject<FoodTruck>()

                        tvPresentationTitle.text=rName
                        tvPresentationBread.text=rInfo
                        Glide.with(this).load(rImge).into(ivFoodtruck)

                    if (truck != null) {

                        tvPresentationTitle.text = truck.companyName
                        tvPresentationBread.text = truck.info
                        Glide.with(this).load(truck?.userPicID).into(ivFoodtruck)
                    }
                        tvPresentationTitle.text = truck?.companyName
                        tvPresentationBread.text = truck?.info
                        Glide.with(this).load(truck?.userPicID).into(ivFoodtruck)
                    }

                }






        }
    }


