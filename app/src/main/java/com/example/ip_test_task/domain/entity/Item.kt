package com.example.ip_test_task.domain.entity

data class Item (
    val id: Int,
    val name: String,
    val time: String,
    val tags: List<String>,
    val amount: Int
)