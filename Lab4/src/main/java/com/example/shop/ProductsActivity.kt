package com.example.shop

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProductsActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        db = DatabaseHelper(this)
        buildList()
    }

    private fun buildList() {
        val layout = findViewById<LinearLayout>(R.id.layoutProducts)
        layout.removeAllViews()
        db.getAllProducts().forEach { product ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(20, 16, 20, 16)
                setBackgroundColor(Color.WHITE)
                val p = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                p.setMargins(0, 0, 0, 12); layoutParams = p
            }
            card.addView(TextView(this).apply {
                text = product.name; textSize = 16f
                setTextColor(Color.parseColor("#6200EE"))
                setTypeface(null, android.graphics.Typeface.BOLD)
            })
            card.addView(TextView(this).apply {
                text = "🏷 ${product.categoryName} | 💰 ${product.price} грн | 📦 ${product.stock} шт"
                textSize = 13f; setTextColor(Color.GRAY)
            })
            val btnReview = Button(this).apply {
                text = "Відгуки"
                backgroundTintList = android.content.res.ColorStateList.valueOf(
                    Color.parseColor("#03DAC5"))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            }
            btnReview.setOnClickListener {
                val intent = Intent(this, ReviewsActivity::class.java)
                intent.putExtra("productId", product.id)
                intent.putExtra("productName", product.name)
                startActivity(intent)
            }
            card.addView(btnReview)
            layout.addView(card)
        }
    }
}
