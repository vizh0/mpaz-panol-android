package com.example.mpaz_panol.data.repository

import com.example.mpaz_panol.data.local.dao.ItemDao
import com.example.mpaz_panol.data.local.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

class InventoryRepository(private val itemDao: ItemDao) {
    val allItems: Flow<List<ItemEntity>> = itemDao.getAllItems()

    suspend fun insertItem(item: ItemEntity) = itemDao.insertItem(item)
    suspend fun updateItem(item: ItemEntity) = itemDao.updateItem(item)
    suspend fun deleteItem(item: ItemEntity) = itemDao.deleteItem(item)
}