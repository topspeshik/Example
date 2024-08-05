package com.example.ip_test_task.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.example.ip_test_task.data.model.ItemDbModel
import com.example.ip_test_task.domain.entity.Item
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao {

    @Query("SELECT * FROM item")
    fun getItems() : Flow<List<ItemDbModel>>

    @Update
    fun updateItem(item: ItemDbModel)

    @Delete
    fun deleteItem(item: ItemDbModel)
}