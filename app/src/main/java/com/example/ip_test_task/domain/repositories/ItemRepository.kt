package com.example.ip_test_task.domain.repositories

import com.example.ip_test_task.data.model.ItemDbModel
import com.example.ip_test_task.domain.entity.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    val itemList: Flow<List<Item>>

    fun changeAmount(item: Item)

    fun deleteItem(item: Item)
}