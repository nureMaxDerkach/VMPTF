package com.example.courses

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MyCoursesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_courses)

        val layout = findViewById<LinearLayout>(R.id.layoutMyCourses)
        val myCourses = AppData.getUserCourses(AppData.currentUser)

        if (myCourses.isEmpty()) {
            val tv = TextView(this).apply {
                text = "Ви ще не записані на жодний курс."
                textSize = 15f
                setTextColor(Color.GRAY)
                setPadding(0, 32, 0, 0)
            }
            layout.addView(tv)
            return
        }

        myCourses.forEach { course ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(20, 20, 20, 20)
                setBackgroundColor(Color.WHITE)
                val p = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                p.setMargins(0, 0, 0, 16)
                layoutParams = p
            }

            val tvTitle = TextView(this).apply {
                text = course.title
                textSize = 17f
                setTextColor(Color.parseColor("#6200EE"))
                setTypeface(null, android.graphics.Typeface.BOLD)
            }

            val statusColor = when (course.status) {
                "completed"   -> "#22C55E"
                "in_progress" -> "#F59E0B"
                else          -> "#888888"
            }
            val statusText = when (course.status) {
                "completed"   -> "✅ Завершено"
                "in_progress" -> "🔄 В процесі"
                else          -> "⏸ Не розпочато"
            }

            val tvStatus = TextView(this).apply {
                text = statusText
                textSize = 14f
                setTextColor(Color.parseColor(statusColor))
            }

            val btnRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 12, 0, 0)
            }

            val btnInProgress = Button(this).apply {
                text = "В процесі"
                textSize = 11f
                backgroundTintList = android.content.res.ColorStateList.valueOf(
                    Color.parseColor("#F59E0B"))
                val p = LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                p.setMargins(0, 0, 8, 0)
                layoutParams = p
            }

            val btnComplete = Button(this).apply {
                text = "Завершити"
                textSize = 11f
                backgroundTintList = android.content.res.ColorStateList.valueOf(
                    Color.parseColor("#22C55E"))
                layoutParams = LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            btnInProgress.setOnClickListener {
                course.status = "in_progress"
                recreate()
            }

            btnComplete.setOnClickListener {
                course.status = "completed"
                recreate()
            }

            btnRow.addView(btnInProgress)
            btnRow.addView(btnComplete)

            card.addView(tvTitle)
            card.addView(tvStatus)
            card.addView(btnRow)
            layout.addView(card)
        }
    }
}
