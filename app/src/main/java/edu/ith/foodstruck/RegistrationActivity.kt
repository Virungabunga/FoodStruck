package edu.ith.foodstruck

import FoodTruck
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {
    lateinit var etFoodtruckName : EditText
    lateinit var etFoodTruckBread : EditText
    lateinit var ivUpload : ImageView
    lateinit var etLatitude : EditText
    lateinit var etLongitude : EditText
    lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        etFoodtruckName=findViewById(R.id.etFoodtruckName)
        etFoodTruckBread=findViewById(R.id.etFoodtruckBread)
        etLatitude=findViewById(R.id.etLatitude)
        etLongitude=findViewById(R.id.etLongitude)
        db = Firebase.firestore

        var buttonSave =findViewById<Button>(R.id.buttonSave)

        buttonSave.setOnClickListener(){
          addFoodTruck()

        }
    }



    fun addFoodTruck( ) {

        var foodtruckName = etFoodtruckName.text.toString()
        var foodtruckBread = etFoodTruckBread.text.toString()
        var latitude = etLatitude.text.toString().toDouble()
        var longitude = etLongitude.text.toString().toDouble()

        var truck =FoodTruck(
            foodtruckName,
            foodtruckBread,
            R.drawable.smallicon,
            longitude,
            latitude,)
            db.collection("FoodTruck")
                .add(truck)
                .addOnSuccessListener {
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }




    }
}