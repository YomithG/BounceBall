package com.example.bounceball


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class GameRegister : AppCompatActivity() {


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_register)

        // Initialize EditText and Button
        editText = findViewById(R.id.editTextText45)
        val continueBtn = findViewById<Button>(R.id.continueBtn)


        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        // Set click listener for continue button
        continueBtn.setOnClickListener {
            val playerName = editText.text.toString()
            savePlayerName(playerName)
            startActivity(Intent(this, MainActivity::class.java))
        }
    }



    // Function to save player's name in SharedPreferences

    private fun savePlayerName(playerName: String) {
        val editor = sharedPreferences.edit()
        editor.putString("playerName", playerName)
        editor.apply()
    }
}