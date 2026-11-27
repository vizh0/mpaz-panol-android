package com.example.mpaz_panol.data.repository

import com.example.mpaz_panol.data.local.dao.ItemDao
import com.example.mpaz_panol.data.local.entity.ItemEntity
import com.example.mpaz_panol.data.remote.EquipoDto
import com.example.mpaz_panol.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow

class InventoryRepository(private val itemDao: ItemDao) {
    val allItems: Flow<List<ItemEntity>> = itemDao.getAllItems()

    suspend fun insertItem(item: ItemEntity) {
        itemDao.insertItem(item)
        try {
            RetrofitClient.equiposApi.crearEquipo(
                EquipoDto(
                    nombre = item.name,
                    marca = item.category,
                    modelo = item.location,
                    stockTotal = item.totalStock,
                    estado = item.status
                )
            )
        } catch (_: Exception) { }
    }

    suspend fun updateItem(item: ItemEntity) {
        itemDao.updateItem(item)
        try {
            if (item.id > 0) {
                RetrofitClient.equiposApi.actualizarEquipo(
                    item.id.toLong(),
                    EquipoDto(
                        id = item.id.toLong(),
                        nombre = item.name,
                        marca = item.category,
                        modelo = item.location,
                        stockTotal = item.totalStock,
                        estado = item.status
                    )
                )
            }
        } catch (_: Exception) { }
    }

    suspend fun deleteItem(item: ItemEntity) {
        itemDao.deleteItem(item)
        try {
            if (item.id > 0) {
                RetrofitClient.equiposApi.eliminarEquipo(item.id.toLong())
            }
        } catch (_: Exception) { }
    }
}