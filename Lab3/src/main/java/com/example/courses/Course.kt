package com.example.courses

data class Review(
    val username: String,
    val rating: Int,
    val comment: String
)

data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val instructor: String,
    var status: String = "not_enrolled",
    val reviews: MutableList<Review> = mutableListOf()
) {
    fun avgRating(): Float {
        if (reviews.isEmpty()) return 0f
        return reviews.sumOf { it.rating }.toFloat() / reviews.size
    }
}
