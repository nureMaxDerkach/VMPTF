package com.example.shop.models

data class User(
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String = "",
    val createdAt: String = ""
)
