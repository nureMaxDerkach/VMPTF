package com.example.shop.models

data class Review(
    val id: Int = 0,
    val userId: Int,
    val productId: Int,
    val rating: Int,
    val comment: String = "",
    val createdAt: String = "",
    val userName: String = "",
    val productName: String = ""
)
