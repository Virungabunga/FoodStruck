package edu.ith.foodstruck

//Removed unused imports
import FoodTruck
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PresentationActivity : AppCompatActivity() {
    private lateinit var ivFoodtruck: ImageView
    private lateinit var tvPresentationTitle: TextView
    private lateinit var tvPresentationBread: TextView

    private lateinit var tvSignatureName: TextView
    lateinit var tvSignatureDescription: TextView
    lateinit var tvSignaturePrice: TextView
    lateinit var ivSignatureImage: ImageView

    lateinit var tvFavoriteName: TextView
    lateinit var tvFavoriteDescription: TextView
    lateinit var tvFavoritePrice: TextView
    lateinit var ivFavoriteImage: ImageView

    lateinit var tvWildName: TextView
    lateinit var tvWildDescription: TextView
    lateinit var tvWildPrice: TextView
    lateinit var ivWildImage: ImageView
    lateinit var truck: FoodTruck
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation)
        supportActionBar?.setHomeButtonEnabled(true)
        ivFoodtruck = findViewById(R.id.ivFoodtruck)
        tvPresentationTitle = findViewById(R.id.tvPresentationTitle)
        tvPresentationBread = findViewById(R.id.tvPresentationBread)
        db = Firebase.firestore
        auth= FirebaseAuth.getInstance()
        tvSignatureDescription = findViewById(R.id.tvSignatureDescription)
        tvSignatureName = findViewById(R.id.tvSignatureName)
        tvSignaturePrice = findViewById(R.id.tvSignaturePrice)
        ivSignatureImage = findViewById(R.id.ivSignature)
        tvFavoriteName = findViewById(R.id.tvFavoriteName)
        tvFavoriteDescription = findViewById(R.id.tvFavoriteDescription)
        tvFavoritePrice = findViewById(R.id.tvFavoritePrice)
        ivFavoriteImage = findViewById(R.id.ivFavorite)
        tvWildName = findViewById(R.id.tvWildName)
        tvWildDescription = findViewById(R.id.tvWildDescription)
        tvWildPrice = findViewById(R.id.tvWildPrice)
        ivWildImage = findViewById(R.id.ivWild)
        val rName = intent.getStringExtra("NAME")
        val rInfo = intent.getStringExtra("INFO")
        val rImge = intent.getStringExtra("IMAGE")

        tvPresentationTitle.text=rName
        tvPresentationBread.text=rInfo

        //Removed unused userID variable

        //Removed unused code


        Glide.with(this).load(rImge).into(ivFoodtruck)
        val foodTruckDocumentId = intent.getStringExtra("FoodTruckID")

        if (foodTruckDocumentId != null) {
            db.collection("FoodTruck").document(foodTruckDocumentId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                     truck = documentSnapshot.toObject<FoodTruck>()!!

                    // Condition "truck!= null" is always true - simplified condition

                    db.collection("users").document(truck.userID!!)
                        .collection("Menu").get()
                        .addOnSuccessListener { snapshot ->
                            truck.menuList = mutableListOf()
                            Log.d("!!!",snapshot.documents.size.toString())
                            for( Food in snapshot.documents) {
                                val menuItem = Food.toObject<Food>()

                                if (menuItem != null) {
                                    truck.menuList!!.add(menuItem)

                                }
                            }

                            setMenu()

                        }

                    tvPresentationTitle.text = rName
                    tvPresentationBread.text = rInfo
                    Glide.with(this).load(rImge).into(ivFoodtruck)

                    // Condition "truck!= null" is always true - simplified condition

                    tvPresentationTitle.text = truck.companyName
                    tvPresentationBread.text = truck.info
                    Glide.with(this).load(truck.userPicID).into(ivFoodtruck)
                    tvPresentationTitle.text = truck.companyName
                    tvPresentationBread.text = truck.info

                    Glide.with(this).load(truck.userPicID).into(ivFoodtruck)
                }

        }


    }


    fun setMenu(){
        tvSignatureName.text= truck.menuList!![0].name
        tvSignatureDescription.text= truck.menuList!![0].description
        tvSignaturePrice.text= truck.menuList!![0].price.toString()

        tvWildName.text= truck.menuList!![1].name
        tvWildDescription.text= truck.menuList!![1].description
        tvWildPrice.text= truck.menuList!![1].price.toString()

        tvFavoriteName.text= truck.menuList!![2].name
        tvFavoriteDescription.text= truck.menuList!![2].description
        tvFavoritePrice.text= truck.menuList!![2].price.toString()

        Glide.with(this).load(truck.menuList!![2].picture).into(ivSignatureImage)
        Glide.with(this).load(truck.menuList!![1].picture).into(ivWildImage)
        Glide.with(this).load(truck.menuList!![0].picture).into(ivFavoriteImage)

    }


}


