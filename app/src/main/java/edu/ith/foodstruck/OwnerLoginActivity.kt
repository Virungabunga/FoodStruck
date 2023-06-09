package edu.ith.foodstruck

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class OwnerLoginActivity : AppCompatActivity() {
    lateinit var etUserName : EditText
    lateinit var etUserPassword : EditText
    private lateinit var tvNotRegistered : TextView
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_login)
        etUserName=findViewById(R.id.etUserName)
        etUserPassword=findViewById(R.id.etUserPassword)
        tvNotRegistered=findViewById(R.id.tvNotRegistrered)
        firebaseAuth = FirebaseAuth.getInstance()
        val buttonLogin=findViewById<Button>(R.id.buttonLogin)
        firebaseAuth.currentUser?.uid

        tvNotRegistered.setOnClickListener(){
            val intent = Intent(this,OwnerSignUpActivity::class.java)
            startActivity(intent)
        }
        buttonLogin.setOnClickListener(){
            loginUser()
        }

    }
    private fun loginUser(){
        val userName = etUserName.text.toString()
        val userPassword = etUserPassword.text.toString()

        if (userName.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            if (userPassword.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(userName,userPassword)
                    .addOnSuccessListener {
                        val intent=Intent(this,MainActivity::class.java)
                        intent.putExtra("CURRENT_USER",firebaseAuth.currentUser?.uid)
                        startActivity(intent)
                    }

                //Replaced "setError" with built in "error" function

            } else if (userPassword.isEmpty()) {
                etUserPassword.error = "Empty fields are not allowed"

            } else if (userName.isEmpty()) {
                etUserName.error = "Empty fields are not allowed"

            }
        }
    }
}