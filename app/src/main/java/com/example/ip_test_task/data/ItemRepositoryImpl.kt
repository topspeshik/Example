package com.example.ip_test_task.data

import com.example.ip_test_task.data.dao.ItemDao
import com.example.ip_test_task.data.model.ItemDbModel
import com.example.ip_test_task.data.model.toEntity
import com.example.ip_test_task.data.model.toModel
import com.example.ip_test_task.domain.entity.Item
import com.example.ip_test_task.domain.repositories.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val itemDao: ItemDao
) : ItemRepository {
    override val itemList: Flow<List<Item>>
        get() = itemDao.getItems().map { it.map { it.toEntity() } }

    override fun changeAmount(item: Item) {
        itemDao.updateItem(item.toModel())
    }

    override fun deleteItem(item: Item) {
        itemDao.deleteItem(item.toModel())
    }
}