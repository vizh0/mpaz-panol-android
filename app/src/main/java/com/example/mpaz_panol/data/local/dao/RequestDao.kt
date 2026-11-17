package com.example.mpaz_panol.data.local.dao

import androidx.room.*
import com.example.mpaz_panol.data.local.entity.RequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {
    @Query("SELECT * FROM requests WHERE userId = :userId ORDER BY createdAt DESC")
    fun getRequestsByUser(userId: Int): Flow<List<RequestEntity>>

    @Query("SELECT * FROM requests ORDER BY createdAt DESC")
    fun getAllRequests(): Flow<List<RequestEntity>>

    @Insert
    suspend fun insertRequest(request: RequestEntity)

    @Update
    suspend fun updateRequest(request: RequestEntity)
}