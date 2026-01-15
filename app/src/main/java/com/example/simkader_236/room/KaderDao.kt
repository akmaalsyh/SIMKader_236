package com.example.simkader_236.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface KaderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(kader: Kader)

    @Update
    suspend fun update(kader: Kader)

    @Delete
    suspend fun delete(kader: Kader)

    @Query("SELECT * from kader_table WHERE id_kader = :id")
    fun getKader(id: Int): Flow<Kader>

    @Query("SELECT * from kader_table ORDER BY nama ASC")
    fun getAllKader(): Flow<List<Kader>>
}