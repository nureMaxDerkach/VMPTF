package com.example.shop.models

data class OrderItem(
    val productId: Int,
    val productName: String,
    val quantity: Int,
    val price: Double
)

data class Order(
    val id: Int = 0,
    val userId: Int,
    val status: String = "pending",
    val total: Double = 0.0,
    val createdAt: String = "",
    val userName: String = "",
    val items: List<OrderItem> = emptyList()
)
