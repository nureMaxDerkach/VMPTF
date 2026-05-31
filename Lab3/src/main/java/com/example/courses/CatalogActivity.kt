package com.example.courses

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class CatalogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        val tvUserName   = findViewById<TextView>(R.id.tvUserName)
        val btnMyCourses = findViewById<Button>(R.id.btnMyCourses)
        val layout = findViewById<LinearLayout>(R.id.layoutCourses)

        tvUserName.text = "👤 ${AppData.currentUser}"

        btnMyCourses.setOnClickListener {
            startActivity(Intent(this, MyCoursesActivity::class.java))
        }

        buildCourseList(layout)
    }

    override fun onResume() {
        super.onResume()
        val layout = findViewById<LinearLayout>(R.id.layoutCourses)
        layout.removeAllViews()
        buildCourseList(layout)
    }

    private fun buildCourseList(layout: LinearLayout) {
        AppData.courses.forEach { course ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(20, 20, 20, 20)
                setBackgroundColor(Color.WHITE)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 16)
                layoutParams = params
            }

            val tvTitle = TextView(this).apply {
                text = course.title
                textSize = 17f
                setTextColor(Color.parseColor("#6200EE"))
                setTypeface(null, android.graphics.Typeface.BOLD)
            }

            val tvDesc = TextView(this).apply {
                text = course.description
                textSize = 13f
                setTextColor(Color.GRAY)
            }

            val tvInstructor = TextView(this).apply {
                text = "👨‍🏫 ${course.instructor}"
                textSize = 13f
            }

            val avgRating = course.avgRating()
            val tvRating = TextView(this).apply {
                text = if (avgRating > 0)
                    "⭐ ${"%.1f".format(avgRating)} (${course.reviews.size} відгуків)"
                else "⭐ Немає оцінок"
                textSize = 13f
            }

            val btnRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 12, 0, 0)
            }

            val enrolled = AppData.isEnrolled(AppData.currentUser, course.id)
            val btnEnroll = Button(this).apply {
                text = if (enrolled) "Записано ✓" else "Записатися"
                isEnabled = !enrolled
                val p = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                p.setMargins(0, 0, 8, 0)
                layoutParams = p
            }

            val btnReviews = Button(this).apply {
                text = "Відгуки"
                backgroundTintList = android.content.res.ColorStateList.valueOf(
                    Color.parseColor("#03DAC5"))
                val p = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                layoutParams = p
            }

            btnEnroll.setOnClickListener {
                AppData.enrollUser(AppData.currentUser, course.id)
                Toast.makeText(this, "Записано на «${course.title}»!", Toast.LENGTH_SHORT).show()
                val layout2 = findViewById<LinearLayout>(R.id.layoutCourses)
                layout2.removeAllViews()
                buildCourseList(layout2)
            }

            btnReviews.setOnClickListener {
                val intent = Intent(this, ReviewActivity::class.java)
                intent.putExtra("courseId", course.id)
                startActivity(intent)
            }

            btnRow.addView(btnEnroll)
            btnRow.addView(btnReviews)

            card.addView(tvTitle)
            card.addView(tvDesc)
            card.addView(tvInstructor)
            card.addView(tvRating)
            card.addView(btnRow)
            layout.addView(card)
        }
    }
}
