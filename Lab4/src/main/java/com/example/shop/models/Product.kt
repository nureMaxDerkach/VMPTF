package com.example.shop.models

data class Product(
    val id: Int = 0,
    val categoryId: Int,
    val name: String,
    val description: String = "",
    val price: Double,
    val stock: Int = 0,
    val categoryName: String = ""
)
