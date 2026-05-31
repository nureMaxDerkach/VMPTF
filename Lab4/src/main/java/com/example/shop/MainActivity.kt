package com.example.shop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var stats: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHelper(this)
        stats = findViewById(R.id.tvStats)

        findViewById<Button>(R.id.btnProducts).setOnClickListener {
            startActivity(Intent(this, ProductsActivity::class.java))
        }
        findViewById<Button>(R.id.btnAddOrder).setOnClickListener {
            startActivity(Intent(this, AddOrderActivity::class.java))
        }
        findViewById<Button>(R.id.btnOrders).setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val users = db.getAllUsers().size
        val products = db.getAllProducts().size
        val orders = db.getAllOrders().size
        val cats = db.getAllCategories().size
        stats.text = "Користувачів: $users | Товарів: $products\nЗамовлень: $orders | Категорій: $cats"
    }
}
