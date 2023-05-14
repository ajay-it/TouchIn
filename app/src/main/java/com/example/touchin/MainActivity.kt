package com.example.touchin

import android.content.ClipData.Item
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.touchin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar(binding.toolbar)

        val auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        if(auth.currentUser == null){
            goLoginActivity()
        }

        db.collection("users").get()
            .addOnSuccessListener { documents ->
                val userList = mutableListOf<ItemModel>()
                for (document in documents) {
                    val name = document.getString("name").toString()
                    val location = document.getString("location").toString()
                    val organization = document.getString("organisation").toString()
                    if (name != null && location != null && organization != null) {
                        userList.add(ItemModel(name, location, organization))
                    }
                }

                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                binding.recyclerView.adapter = ItemAdapter(this, userList)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
                Toast.makeText(this, "Error in displaying", Toast.LENGTH_SHORT).show()
            }


//        val itemList = listOf(
//            ItemModel("Ajay", "Prayagraj", "AKTU"),
//            ItemModel("Shubham", "Jhansi", "BU"),
//            ItemModel("Karan", "Gorakhpur", "AKTU"),
//            ItemModel("Gantavya", "Bhopal", "LNCT"),
//            ItemModel("Jitesh", "Bhopal", "MLM"),
//            ItemModel("Belinda Li", "Australia", "AU"),
//            ItemModel("Ajay", "Prayagraj", "AKTU"),
//            ItemModel("Shubham", "Jhansi", "BU"),
//            ItemModel("Karan", "Gorakhpur", "AKTU"),
//            ItemModel("Gantavya", "Bhopal", "LNCT"),
//            ItemModel("Jitesh", "Bhopal", "MLM"),
//            ItemModel("Belinda Li", "Australia", "AU"),
//        )
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = ItemAdapter(this, itemList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                // Call the signOut method of FirebaseAuth to log out the user
                FirebaseAuth.getInstance().signOut()
                // Navigate the user back to the login screen
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    private fun goLoginActivity() {
        Log.i(TAG, "goLoginActivity")
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
}