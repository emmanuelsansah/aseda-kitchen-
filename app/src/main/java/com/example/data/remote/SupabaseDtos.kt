package com.example.data.remote

import com.example.data.MenuItemEntity
import com.example.data.OrderEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SupabaseMenuItemDto(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "price_ghs") val priceGhs: Double,
    @Json(name = "category") val category: String,
    @Json(name = "origin") val origin: String,
    @Json(name = "drawable_res_name") val drawableResName: String = "img_hero_banner",
    @Json(name = "image_url") val imageUrl: String = "",
    @Json(name = "is_available") val isAvailable: Boolean = true,
    @Json(name = "is_featured") val isFeatured: Boolean = false
) {
    fun toEntity(): MenuItemEntity {
        return MenuItemEntity(
            id = id ?: 0,
            name = name,
            description = description,
            priceGhs = priceGhs,
            category = category,
            origin = origin,
            drawableResName = drawableResName,
            imageUrl = imageUrl,
            isAvailable = isAvailable,
            isFeatured = isFeatured
        )
    }

    companion object {
        fun fromEntity(entity: MenuItemEntity): SupabaseMenuItemDto {
            return SupabaseMenuItemDto(
                id = if (entity.id == 0) null else entity.id,
                name = entity.name,
                description = entity.description,
                priceGhs = entity.priceGhs,
                category = entity.category,
                origin = entity.origin,
                drawableResName = entity.drawableResName,
                imageUrl = entity.imageUrl,
                isAvailable = entity.isAvailable,
                isFeatured = entity.isFeatured
            )
        }
    }
}

@JsonClass(generateAdapter = true)
data class SupabaseOrderDto(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "customer_name") val customerName: String,
    @Json(name = "customer_phone") val customerPhone: String,
    @Json(name = "delivery_address") val deliveryAddress: String,
    @Json(name = "item_name") val itemName: String,
    @Json(name = "quantity") val quantity: Int,
    @Json(name = "total_price_ghs") val totalPriceGhs: Double,
    @Json(name = "notes") val notes: String = "",
    @Json(name = "status") val status: String = "WhatsApp Order Placed"
) {
    fun toEntity(): OrderEntity {
        return OrderEntity(
            id = id ?: 0,
            customerName = customerName,
            customerPhone = customerPhone,
            deliveryAddress = deliveryAddress,
            itemName = itemName,
            quantity = quantity,
            totalPriceGhs = totalPriceGhs,
            notes = notes,
            status = status
        )
    }

    companion object {
        fun fromEntity(entity: OrderEntity): SupabaseOrderDto {
            return SupabaseOrderDto(
                id = if (entity.id == 0) null else entity.id,
                customerName = entity.customerName,
                customerPhone = entity.customerPhone,
                deliveryAddress = entity.deliveryAddress,
                itemName = entity.itemName,
                quantity = entity.quantity,
                totalPriceGhs = entity.totalPriceGhs,
                notes = entity.notes,
                status = entity.status
            )
        }
    }
}
