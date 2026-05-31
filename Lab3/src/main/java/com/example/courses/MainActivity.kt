package com.example.courses

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val btnEnter   = findViewById<Button>(R.id.btnEnter)

        btnEnter.setOnClickListener {
            val name = etUsername.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Введіть ім'я!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            AppData.currentUser = name
            startActivity(Intent(this, CatalogActivity::class.java))
        }
    }
}
