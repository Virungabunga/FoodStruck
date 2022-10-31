package edu.ith.foodstruck

import FoodTruck
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class RegistrationActivity : AppCompatActivity() {
    lateinit var etFoodtruckName : EditText
    lateinit var etFoodTruckBread : EditText
    lateinit var ivUpload : ImageView
    lateinit var tVuploadFromGallery : TextView
    lateinit var etLatitude : EditText
    lateinit var etLongitude : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        etFoodtruckName=findViewById(R.id.etFoodtruckName)
        etFoodTruckBread=findViewById(R.id.etFoodtruckBread)
        ivUpload=findViewById(R.id.ivFoodtruck)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        tVuploadFromGallery.setOnClickListener(){

        }

        var buttonSave =findViewById<Button>(R.id.buttonSave)

        buttonSave.setOnClickListener(){
            var foodtruckName = etFoodtruckName.text.toString()
            var foodtruckBread = etFoodTruckBread.text.toString()
            var latitude = etLatitude.text.toString().toDouble()
            var longitude = etLongitude.text.toString().toDouble()
                    var truck =FoodTruck(
                        foodtruckName,
                        foodtruckBread,
                        R.drawable.smallicon,
                        longitude,
                        latitude,







                    )
        }
    }
}




fun addFoodTruck(var name : String,
                 var info : String,
                 var picId : Int,
                 var lat : Double,
                 var long: Double) {
    var kosayFoodTruck = FoodTruck(
        "Kosays fine dining",
        "Vi startade 1995",
        R.drawable.smallicon,
        59.20570928820239, 17.818639780606336
    )

    db.collection("FoodTruck")
        .add(kosayFoodTruck)
}