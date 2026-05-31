package com.example.multiply

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNumber1 = findViewById<EditText>(R.id.etNumber1)
        val etNumber2 = findViewById<EditText>(R.id.etNumber2)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val btnGoToGame = findViewById<Button>(R.id.btnGoToGame)

        btnCalculate.setOnClickListener {
            val input1 = etNumber1.text.toString()
            val input2 = etNumber2.text.toString()

            if (input1.isEmpty() || input2.isEmpty()) {
                Toast.makeText(this, "Введіть обидва числа!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val num1 = input1.toDoubleOrNull()
            val num2 = input2.toDoubleOrNull()

            if (num1 == null || num2 == null) {
                Toast.makeText(this, "Некоректне число!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = num1 * num2
            tvResult.text = "$num1 × $num2 = $result"
        }

        btnGoToGame.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
    }
}
