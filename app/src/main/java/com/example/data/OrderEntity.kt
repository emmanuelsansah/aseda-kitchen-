package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val itemName: String,
    val quantity: Int,
    val totalPriceGhs: Double,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "WhatsApp Order Placed"
)
