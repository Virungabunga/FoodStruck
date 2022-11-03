package edu.ith.foodstruck

import FoodTruck
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import edu.ith.foodstruck.databinding.ActivityMainBinding
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var tvSmallRating: TextView
    lateinit var cardView: CardView
    lateinit var smalltitle: TextView
    lateinit var ivSmallInfo:ImageView
    private val REQUEST_LOCATION = 1
    lateinit var locationProvider: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback
    lateinit var locationRequest: LocationRequest
    private lateinit var auth: FirebaseAuth
    private lateinit var loginView:TextView
    private lateinit var navMenu:NavigationView
    private lateinit var addView: ImageView
    private var myPos :LatLng =LatLng(59.0,18.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


       navMenu = findViewById(R.id.navView)

        auth = Firebase.auth
        db = Firebase.firestore
        tvSmallRating = findViewById(R.id.tv_small_rating)
        smalltitle = findViewById(R.id.tv_small_title)
        cardView = findViewById(R.id.cardView  )
        ivSmallInfo=findViewById(R.id.iv_small_info)
        loginView=findViewById(R.id.loginView)
        addView=findViewById(R.id.addView)



        signInAnonymously()
        updateUI()
        navMenu()
        locationData()
        checkPermission()

        ivSmallInfo.setOnClickListener{
            intentToPresentation()
        }

    }



    private fun addToFavorites(truck: FoodTruck) {

        if(auth.currentUser!=null){
       uploadFavorites(truck, auth.currentUser!!.uid)}
    }

    fun uploadFavorites(foodTruck: FoodTruck,userId:String){

        db.collection("users").document(userId).collection("favorites")
            .add(foodTruck)
            .addOnSuccessListener {
                Toast.makeText(this, "Saved to Favorites", Toast.LENGTH_SHORT).show()
            }

    }
    private fun signInAnonymously(){
        if(auth.currentUser==null){
            auth.signInAnonymously()
        }
    }


    private fun locationData() {


        locationProvider = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = createLocationRequest()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                myPos=LatLng(locationResult.lastLocation.latitude,locationResult.lastLocation.longitude)
                for (location in locationResult.locations) {


               }
           }
       }
    }

    private fun checkPermission() {


        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
        }

    }



    private fun navMenu(){
        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                R.string.open,
                R.string.close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.firstItem -> {

                        val intent =Intent(this@MainActivity,FavoritesActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.secondtItem -> {
                        val intent = Intent(this@MainActivity,FoodtruckListActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this@MainActivity, "Second Item Clicked", Toast.LENGTH_SHORT)
                            .show()
                    }
                    R.id.thirdItem -> {

                        var intent =Intent(this@MainActivity,OwnerLoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this@MainActivity, "taking you to the sign up/login", Toast.LENGTH_SHORT)
                            .show()

                    }
                    R.id.fourthItem -> {

                        singOut()
                        Toast.makeText(this@MainActivity, "Signing out:(", Toast.LENGTH_SHORT)
                            .show()

                    }
                    R.id.fifthItem -> {
                        var intent =Intent(this@MainActivity,RegistrationActivity::class.java)
                        startActivity(intent)

                        Toast.makeText(this@MainActivity, "Register", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
                true
            }
        }
    }

    private fun singOut(){
       auth.signOut()
        updateUI()
    }

    private fun updateUI (){
        if(auth.currentUser != null) {
            navMenu.menu.findItem(R.id.fifthItem).isVisible = auth.currentUser?.isAnonymous != true
            Log.d("!!!!", "${auth.currentUser?.email}")
            loginView.text="${auth.currentUser?.email}"
            loginView.isVisible =true
            navMenu.menu.findItem(R.id.fourthItem).isVisible=true




        }  else {
            loginView.isVisible=false
            navMenu.menu.findItem(R.id.fourthItem).isVisible=false
            navMenu.menu.findItem(R.id.fifthItem).isVisible=false
        }
    }




    private fun distanceToTruck(truck: FoodTruck): Float {
        val result = FloatArray(2)



        Location.distanceBetween(myPos.latitude, myPos.longitude,truck.lat!! ,truck.long!! , result)
        return result[0]
    }


    private fun intentToPresentation(){
        val intent=Intent(this,PresentationActivity::class.java)
        startActivity(intent)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }


    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }


    fun createLocationRequest(): LocationRequest =
        LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


    private fun stopLocationUpdates() {
        locationProvider.removeLocationUpdates(locationCallback)
    }


    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("!!!", "startlocationUpdates")
            locationProvider.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            }

        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        readFromFirestore()
        mMap = googleMap
        mMap.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(
                    LatLng(
                        59.33507323679574,
                        18.067196534476423,
                    ),
                    10F,
                )
        )
        checkPermission()
        mMap.isMyLocationEnabled = true
        googleMap.setOnMarkerClickListener(this)


    }


    private fun readFromFirestore() {
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

    private fun addMarker(truck: FoodTruck) {

        val markerTruck = mMap.addMarker(

            MarkerOptions().position(LatLng(truck.lat!!, truck.long!!))
                .title(truck.companyName)

                .icon(
                    BitmapDescriptorFactory.fromResource(
                        R.drawable.smallicon
                    )
                )
        )
        markerTruck?.tag = truck
    }

    fun updateCardUI(truck : FoodTruck) {
        smalltitle.text = truck.companyName
        // set pic
        // set price

         cardView.isVisible = true

         val dist = distanceToTruck(truck).roundToInt()
        tvSmallRating.text=dist.toString()+"m"
        Glide.with(this).load(truck.userPicID).into(ivSmallInfo)


    }


    override fun onMarkerClick(p0: Marker): Boolean {



        val truck: FoodTruck? = p0.tag as? FoodTruck

        if (truck != null) {


            updateCardUI(truck)

            addView.setOnClickListener{
                addToFavorites(truck)
            }
            cardView.setOnClickListener(){
                val intent= Intent(this,PresentationActivity::class.java)
                intent.putExtra("FoodTruckID",truck.documentId)
                startActivity(intent)

            }
        }
        return true
    }
}


