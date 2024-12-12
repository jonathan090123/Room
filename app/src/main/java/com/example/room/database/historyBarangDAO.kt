package com.example.room.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.room.historyBarang

@Dao
interface historyBarangDAO {
    @Insert
    suspend fun insert(history: historyBarang)

    @Query("SELECT * FROM historyBarang")
    suspend fun getAll(): List<historyBarang>
}

@Database(entities = [historyBarang::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyBarangDAO(): historyBarangDAO
}
