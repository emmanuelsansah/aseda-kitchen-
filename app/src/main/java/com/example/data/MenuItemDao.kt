package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM menu_items ORDER BY isFeatured DESC, id ASC")
    fun getAllMenuItems(): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_items WHERE id = :id")
    suspend fun getMenuItemById(id: Int): MenuItemEntity?

    @Query("SELECT * FROM menu_items WHERE isFeatured = 1")
    fun getFeaturedMenuItems(): Flow<List<MenuItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(item: MenuItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MenuItemEntity>)

    @Update
    suspend fun updateMenuItem(item: MenuItemEntity)

    @Delete
    suspend fun deleteMenuItem(item: MenuItemEntity)

    @Query("SELECT COUNT(*) FROM menu_items")
    suspend fun getCount(): Int
}
