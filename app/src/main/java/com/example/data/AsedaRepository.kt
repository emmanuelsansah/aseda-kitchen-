package com.example.data

import android.util.Log
import com.example.data.remote.SupabaseClient
import com.example.data.remote.SupabaseMenuItemDto
import com.example.data.remote.SupabaseOrderDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AsedaRepository(
    private val menuItemDao: MenuItemDao,
    private val orderDao: OrderDao
) {
    val allMenuItems: Flow<List<MenuItemEntity>> = menuItemDao.getAllMenuItems()
    val featuredMenuItems: Flow<List<MenuItemEntity>> = menuItemDao.getFeaturedMenuItems()
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()

    suspend fun syncWithSupabase() = withContext(Dispatchers.IO) {
        try {
            // Fetch Menu Items from Supabase Cloud
            val menuResponse = SupabaseClient.apiService.getMenuItems()
            if (menuResponse.isSuccessful && menuResponse.body() != null) {
                val cloudItems = menuResponse.body()!!.map { it.toEntity() }
                if (cloudItems.isNotEmpty()) {
                    menuItemDao.insertAll(cloudItems)
                    Log.d("AsedaRepository", "Successfully synced ${cloudItems.size} menu items from Supabase!")
                }
            }

            // Fetch Orders from Supabase Cloud
            val orderResponse = SupabaseClient.apiService.getOrders()
            if (orderResponse.isSuccessful && orderResponse.body() != null) {
                val cloudOrders = orderResponse.body()!!.map { it.toEntity() }
                cloudOrders.forEach { order ->
                    orderDao.insertOrder(order)
                }
                Log.d("AsedaRepository", "Successfully synced ${cloudOrders.size} orders from Supabase!")
            }
        } catch (e: Exception) {
            Log.e("AsedaRepository", "Supabase sync error (using local Room database): ${e.localizedMessage}", e)
        }
    }

    suspend fun insertMenuItem(item: MenuItemEntity): Long = withContext(Dispatchers.IO) {
        val localId = menuItemDao.insertMenuItem(item)
        try {
            val dto = SupabaseMenuItemDto.fromEntity(item.copy(id = localId.toInt()))
            val response = SupabaseClient.apiService.createMenuItem(dto)
            if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                val returnedDto = response.body()!!.first()
                menuItemDao.insertMenuItem(returnedDto.toEntity())
            }
        } catch (e: Exception) {
            Log.e("AsedaRepository", "Failed to sync created menu item to Supabase: ${e.localizedMessage}", e)
        }
        return@withContext localId
    }

    suspend fun updateMenuItem(item: MenuItemEntity) = withContext(Dispatchers.IO) {
        menuItemDao.updateMenuItem(item)
        try {
            val dto = SupabaseMenuItemDto.fromEntity(item)
            SupabaseClient.apiService.updateMenuItem("eq.${item.id}", dto)
        } catch (e: Exception) {
            Log.e("AsedaRepository", "Failed to update menu item on Supabase: ${e.localizedMessage}", e)
        }
    }

    suspend fun deleteMenuItem(item: MenuItemEntity) = withContext(Dispatchers.IO) {
        menuItemDao.deleteMenuItem(item)
        try {
            SupabaseClient.apiService.deleteMenuItem("eq.${item.id}")
        } catch (e: Exception) {
            Log.e("AsedaRepository", "Failed to delete menu item on Supabase: ${e.localizedMessage}", e)
        }
    }

    suspend fun insertOrder(order: OrderEntity): Long = withContext(Dispatchers.IO) {
        val localId = orderDao.insertOrder(order)
        try {
            val dto = SupabaseOrderDto.fromEntity(order.copy(id = localId.toInt()))
            val response = SupabaseClient.apiService.createOrder(dto)
            if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                val returnedDto = response.body()!!.first()
                orderDao.insertOrder(returnedDto.toEntity())
            }
        } catch (e: Exception) {
            Log.e("AsedaRepository", "Failed to insert order into Supabase: ${e.localizedMessage}", e)
        }
        return@withContext localId
    }

    suspend fun deleteOrder(orderId: Int) = withContext(Dispatchers.IO) {
        orderDao.deleteOrder(orderId)
        try {
            SupabaseClient.apiService.deleteOrder("eq.$orderId")
        } catch (e: Exception) {
            Log.e("AsedaRepository", "Failed to delete order on Supabase: ${e.localizedMessage}", e)
        }
    }

    fun verifyAdminPassword(input: String): Boolean {
        return input == "Asedakitchen"
    }

    suspend fun checkAndSeedIfEmpty() = withContext(Dispatchers.IO) {
        if (menuItemDao.getCount() == 0) {
            AppDatabase.populateInitialDatabase(menuItemDao)
        }
        syncWithSupabase()
    }
}
