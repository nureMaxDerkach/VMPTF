package com.example.shop

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class OrdersActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        db = DatabaseHelper(this)
        buildList()
    }

    private fun buildList() {
        val layout = findViewById<LinearLayout>(R.id.layoutOrders)
        layout.removeAllViews()
        db.getAllOrders().forEach { order ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(20, 16, 20, 16)
                setBackgroundColor(Color.WHITE)
                val p = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                p.setMargins(0, 0, 0, 12); layoutParams = p
            }
            val statusColor = when (order.status) {
                "completed"  -> "#22C55E"
                "cancelled"  -> "#EF4444"
                else         -> "#F59E0B"
            }
            card.addView(TextView(this).apply {
                text = "Замовлення #${order.id}"
                textSize = 15f
                setTypeface(null, android.graphics.Typeface.BOLD)
            })
            card.addView(TextView(this).apply {
                text = "👤 ${order.userName} | 💰 ${order.total} грн"
                textSize = 13f
            })
            card.addView(TextView(this).apply {
                text = "● ${order.status}"
                setTextColor(Color.parseColor(statusColor))
                textSize = 13f
            })

            val btnRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL; setPadding(0, 8, 0, 0)
            }
            val btnComplete = Button(this).apply {
                text = "Виконано"
                backgroundTintList = android.content.res.ColorStateList.valueOf(
                    Color.parseColor("#22C55E"))
                val p = LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                p.setMargins(0, 0, 8, 0); layoutParams = p
            }
            val btnCancel = Button(this).apply {
                text = "Скасувати"
                backgroundTintList = android.content.res.ColorStateList.valueOf(
                    Color.parseColor("#EF4444"))
                layoutParams = LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }
            btnComplete.setOnClickListener {
                db.updateOrderStatus(order.id, "completed")
                buildList()
            }
            btnCancel.setOnClickListener {
                db.updateOrderStatus(order.id, "cancelled")
                buildList()
            }
            btnRow.addView(btnComplete); btnRow.addView(btnCancel)
            card.addView(btnRow)
            layout.addView(card)
        }
    }
}
