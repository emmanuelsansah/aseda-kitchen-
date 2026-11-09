package com.example.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApiService {

    @GET("rest/v1/menu_items?select=*&order=id.asc")
    suspend fun getMenuItems(): Response<List<SupabaseMenuItemDto>>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/menu_items")
    suspend fun createMenuItem(@Body item: SupabaseMenuItemDto): Response<List<SupabaseMenuItemDto>>

    @Headers("Prefer: return=representation")
    @PATCH("rest/v1/menu_items")
    suspend fun updateMenuItem(
        @Query("id") idQuery: String,
        @Body item: SupabaseMenuItemDto
    ): Response<List<SupabaseMenuItemDto>>

    @DELETE("rest/v1/menu_items")
    suspend fun deleteMenuItem(@Query("id") idQuery: String): Response<Unit>

    @GET("rest/v1/orders?select=*&order=created_at.desc")
    suspend fun getOrders(): Response<List<SupabaseOrderDto>>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/orders")
    suspend fun createOrder(@Body order: SupabaseOrderDto): Response<List<SupabaseOrderDto>>

    @DELETE("rest/v1/orders")
    suspend fun deleteOrder(@Query("id") idQuery: String): Response<Unit>
}
