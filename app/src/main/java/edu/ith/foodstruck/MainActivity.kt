package edu.ith.foodstruck

import FoodTruck
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath.documentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import edu.ith.foodstruck.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMainBinding
    private lateinit var db:FirebaseFirestore
    private lateinit var oscarFoodTruck:FoodTruck
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         db = Firebase.firestore
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        oscarFoodTruck = FoodTruck("Oscars Mackor",
            R.drawable.smallicon,
            59.3609701472053, 17.96979995865056,)

        db.collection("FoodTruck")
            .add(oscarFoodTruck)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

       val food1 = db.collection("FoodTruck")
           .get()
            .addOnSuccessListener { documentSnapshot ->
                val foodTruckList = mutableListOf<FoodTruck>()

                for (document in documentSnapshot.documents) {
                    val truck = document.toObject<FoodTruck>()
                    if (truck != null)
                        foodTruckList.add(truck)

                }

               var long = foodTruckList[0].long
                var  lat = foodTruckList[0].lat

                if (long != null) {
                    if (lat != null) {
                        addMarker(lat,long)
                    }
                }

           }

    }



    fun addMarker(lat:Double,long :Double) {

        mMap.addMarker(
            MarkerOptions().position(LatLng(long,lat))
                .title("")
                .icon(
                    BitmapDescriptorFactory.fromResource(
                        R.drawable.smallicon
                    )
                )
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(long,lat)))
    }

}


