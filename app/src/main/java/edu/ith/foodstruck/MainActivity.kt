package edu.ith.foodstruck

import FoodTruck
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import edu.ith.foodstruck.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMainBinding
    private lateinit var db:FirebaseFirestore
    private lateinit var marker:Marker
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var tvSmallRating:TextView
    lateinit var cardView:CardView
    lateinit var smalltitle:TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       //var tvSmallInfo:TextView = findViewById(R.id.tv_small_title)

        //cardView =findViewById(R.id.cardView)
         db = Firebase.firestore

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        tvSmallRating =findViewById(R.id.tv_small_rating)
        smalltitle=findViewById(R.id.tv_small_title)
       cardView=findViewById(R.id.cardView)
        binding.apply {
            toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.firstItem -> {
                        Toast.makeText(this@MainActivity, "First Item Clicked", Toast.LENGTH_SHORT).show()
                    }
                    R.id.secondtItem -> {
                        Toast.makeText(this@MainActivity, "Second Item Clicked", Toast.LENGTH_SHORT).show()
                    }
                    R.id.thirdItem -> {
                        Toast.makeText(this@MainActivity, "third Item Clicked", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        readFromFirestore()
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory
            .newLatLngZoom(
                LatLng(
                    59.33507323679574,
                    18.067196534476423,
                ),
                10F,))

            googleMap.setOnMarkerClickListener(this)




    }



    fun readFromFirestore(){
        db.collection("FoodTruck")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val foodTruckList = mutableListOf<FoodTruck>()

                for (document in documentSnapshot.documents) {
                    val truck = document.toObject<FoodTruck>()
                    if (truck != null)
                        foodTruckList.add(truck)

                }
                for (truck in foodTruckList) {


                    addMarker(truck)
                }

            }
    }
    fun addFoodTruck(){
        var kosayFoodTruck = FoodTruck("Kosays fine dining",
            R.drawable.smallicon,
            59.20570928820239, 17.818639780606336)

        db.collection("FoodTruck")
            .add(kosayFoodTruck)
    }
    fun addMarker(truck: FoodTruck) {//lat:Double,long :Double, title:String) {



            val marker = mMap.addMarker(

                MarkerOptions().position(LatLng(truck.long!!, truck.lat!!))
                    .title(truck.companyName)
                    .icon(
                        BitmapDescriptorFactory.fromResource(
                            R.drawable.smallicon
                        )
                    )
            )

        marker?.tag = truck


    }

    override fun onMarkerClick(p0: Marker): Boolean {
       //tvSmallRating.setText()

        cardView.isVisible = true
        val truck = p0.tag as? FoodTruck

        if (truck != null) {
            smalltitle.setText(truck.companyName)
        }
        return true
    }

}


