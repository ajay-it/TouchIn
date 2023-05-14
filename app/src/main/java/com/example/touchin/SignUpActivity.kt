package com.example.touchin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.touchin.databinding.ActivityMainBinding
import com.example.touchin.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize Firebase authentication instance
        auth = FirebaseAuth.getInstance()

        binding.logInBtn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        binding.signUpBtn.setOnClickListener {
            Toast.makeText(this, "Hehe", Toast.LENGTH_SHORT).show()
            validateData(
                binding.name.text.toString(),
                binding.email.text.toString(),
                binding.phone.text.toString(),
                binding.location.text.toString(),
                binding.organisation.text.toString()
            )
        }
    }

    private fun validateData(
        name: String,
        email: String,
        phone: String,
        location: String,
        organisation: String
    ) {
        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() ||
            location.isEmpty() || organisation.isEmpty())
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        else {
            auth.createUserWithEmailAndPassword(email, phone)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User registered successfully
                        val user = auth.currentUser
                        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        // User registration failed
                        val exception = task.exception
                        // Handle the exception
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }

            storeData(name, email, phone, location, organisation)
        }
    }

    private fun storeData(name: String, email: String, phone: String, location: String, organisation: String) {
        val map = hashMapOf<String, Any>()
        map["name"] = name
        map["email"] = email
        map["phone"] = phone
        map["location"] = location
        map["organisation"] = organisation

        Firebase.firestore.collection("users")
            .document(email)
            .set(map).addOnSuccessListener {

                val b = Bundle()
                b.putString("email", email)
                intent.putExtras(b)

                val intent = Intent(this, LogInActivity::class.java)
                intent.putExtras(b)
                startActivity(intent)
            }.addOnFailureListener{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()

            }
    }

}
