package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val priceGhs: Double,
    val category: String, // "Ghanaian Specialties", "Nigerian Favorites", "Rice & Swallows", "Grills & Soups", "Drinks"
    val origin: String, // "Ghanaian", "Nigerian", "Both"
    val drawableResName: String, // e.g. "img_banku_tilapia", "img_fufu_soup", "img_egusi_pounded_yam", "img_hero_banner"
    val imageUrl: String = "",
    val isAvailable: Boolean = true,
    val isFeatured: Boolean = false
)
