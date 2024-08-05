package com.example.ip_test_task.presentation

import com.example.ip_test_task.data.model.ItemDbModel
import com.example.ip_test_task.domain.entity.Item

sealed class MainScreenState {

    object Initial : MainScreenState()

    data class Items(
        val items: List<Item>
    ) : MainScreenState()
}