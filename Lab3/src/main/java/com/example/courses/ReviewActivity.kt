package com.example.courses

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ReviewActivity : AppCompatActivity() {

    private lateinit var course: Course
    private var selectedRating = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val courseId = intent.getIntExtra("courseId", -1)
        course = AppData.courses.find { it.id == courseId }!!

        val tvTitle     = findViewById<TextView>(R.id.tvCourseTitle)
        val tvRatingLbl = findViewById<TextView>(R.id.tvRatingLabel)
        val seekRating  = findViewById<SeekBar>(R.id.seekRating)
        val etComment   = findViewById<EditText>(R.id.etComment)
        val btnSubmit   = findViewById<Button>(R.id.btnSubmit)

        tvTitle.text = "Відгуки: ${course.title}"

        seekRating.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar, p: Int, u: Boolean) {
                selectedRating = p + 1
                tvRatingLbl.text = "Оцінка: ${"⭐".repeat(selectedRating)}"
            }
            override fun onStartTrackingTouch(s: SeekBar) {}
            override fun onStopTrackingTouch(s: SeekBar) {}
        })

        btnSubmit.setOnClickListener {
            val comment = etComment.text.toString().trim()
            if (comment.isEmpty()) {
                Toast.makeText(this, "Введіть текст відгуку!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            course.reviews.add(Review(AppData.currentUser, selectedRating, comment))
            etComment.text.clear()
            Toast.makeText(this, "Відгук додано!", Toast.LENGTH_SHORT).show()
            buildReviews()
        }

        buildReviews()
    }

    private fun buildReviews() {
        val layout = findViewById<LinearLayout>(R.id.layoutReviews)
        layout.removeAllViews()

        val avg = course.avgRating()
        val tvAvg = TextView(this).apply {
            text = if (avg > 0) "Середня оцінка: ${"⭐".repeat(avg.toInt())} (${"%.1f".format(avg)})"
                   else "Поки немає відгуків"
            textSize = 15f
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 0, 0, 16)
        }
        layout.addView(tvAvg)

        if (course.reviews.isEmpty()) {
            val tv = TextView(this).apply {
                text = "Будьте першим, хто залишить відгук!"
                setTextColor(Color.GRAY)
            }
            layout.addView(tv)
            return
        }

        course.reviews.forEach { review ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
                setBackgroundColor(Color.WHITE)
                val p = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                p.setMargins(0, 0, 0, 12)
                layoutParams = p
            }

            val header = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }
            val tvName = TextView(this).apply {
                text = review.username
                textSize = 14f
                setTypeface(null, Typeface.BOLD)
                layoutParams = LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }
            val tvStars = TextView(this).apply {
                text = "⭐".repeat(review.rating)
                textSize = 14f
                setTextColor(Color.parseColor("#F59E0B"))
            }
            header.addView(tvName)
            header.addView(tvStars)

            val tvComment = TextView(this).apply {
                text = review.comment
                textSize = 13f
                setTextColor(Color.parseColor("#555555"))
                setPadding(0, 8, 0, 0)
            }

            card.addView(header)
            card.addView(tvComment)
            layout.addView(card)
        }
    }
}
