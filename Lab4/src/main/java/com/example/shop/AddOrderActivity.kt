package com.example.shop

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.shop.models.Order
import com.example.shop.models.OrderItem

class AddOrderActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)
        db = DatabaseHelper(this)

        val spinnerUser    = findViewById<Spinner>(R.id.spinnerUser)
        val spinnerProduct = findViewById<Spinner>(R.id.spinnerProduct)
        val etQty          = findViewById<EditText>(R.id.etQuantity)
        val btnSave        = findViewById<Button>(R.id.btnSave)

        val users    = db.getAllUsers()
        val products = db.getAllProducts()

        spinnerUser.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            users.map { it.name }).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerProduct.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            products.map { "${it.name} — ${it.price} грн" }).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        btnSave.setOnClickListener {
            if (users.isEmpty()) {
                Toast.makeText(this, "Немає користувачів!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (products.isEmpty()) {
                Toast.makeText(this, "Немає товарів!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val user    = users[spinnerUser.selectedItemPosition]
            val product = products[spinnerProduct.selectedItemPosition]
            val qty     = etQty.text.toString().toIntOrNull() ?: 1

            val item  = OrderItem(product.id, product.name, qty, product.price)
            val order = Order(userId = user.id)
            db.addOrder(order, listOf(item))

            Toast.makeText(this, "Замовлення додано!", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }
}
