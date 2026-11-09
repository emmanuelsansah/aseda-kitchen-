package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AsedaRepository
import com.example.data.MenuItemEntity
import com.example.data.OrderEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLEncoder

enum class ScreenState {
    HOME,
    MENU,
    CHEF_AI,
    ABOUT,
    CONTACT,
    ADMIN_DASHBOARD
}

data class OrderFormState(
    val customerName: String = "",
    val customerPhone: String = "",
    val deliveryAddress: String = "",
    val quantity: Int = 1,
    val notes: String = ""
)

class AsedaViewModel(private val repository: AsedaRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.checkAndSeedIfEmpty()
        }
    }

    private val _currentScreen = MutableStateFlow(ScreenState.HOME)
    val currentScreen: StateFlow<ScreenState> = _currentScreen

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _selectedOrigin = MutableStateFlow("All")
    val selectedOrigin: StateFlow<String> = _selectedOrigin

    val allMenuItems: StateFlow<List<MenuItemEntity>> = repository.allMenuItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredMenuItems: StateFlow<List<MenuItemEntity>> = repository.featuredMenuItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredMenuItems: StateFlow<List<MenuItemEntity>> = combine(
        repository.allMenuItems,
        _searchQuery,
        _selectedCategory,
        _selectedOrigin
    ) { items, query, category, origin ->
        items.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.name.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true)
            val matchesCategory = category == "All" || item.category.equals(category, ignoreCase = true)
            val matchesOrigin = origin == "All" || item.origin.equals(origin, ignoreCase = true)
            matchesQuery && matchesCategory && matchesOrigin
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Order Modal state
    private val _selectedItemForOrder = MutableStateFlow<MenuItemEntity?>(null)
    val selectedItemForOrder: StateFlow<MenuItemEntity?> = _selectedItemForOrder

    private val _orderFormState = MutableStateFlow(OrderFormState())
    val orderFormState: StateFlow<OrderFormState> = _orderFormState

    // Order History dialog state
    private val _showOrderHistoryDialog = MutableStateFlow(false)
    val showOrderHistoryDialog: StateFlow<Boolean> = _showOrderHistoryDialog

    // Admin Auth State
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated

    private val _showAdminLoginDialog = MutableStateFlow(false)
    val showAdminLoginDialog: StateFlow<Boolean> = _showAdminLoginDialog

    private val _adminPasswordInput = MutableStateFlow("")
    val adminPasswordInput: StateFlow<String> = _adminPasswordInput

    private val _adminPasswordError = MutableStateFlow(false)
    val adminPasswordError: StateFlow<Boolean> = _adminPasswordError

    // Admin Food Add/Edit state
    private val _showAddEditFoodDialog = MutableStateFlow(false)
    val showAddEditFoodDialog: StateFlow<Boolean> = _showAddEditFoodDialog

    private val _editingMenuItem = MutableStateFlow<MenuItemEntity?>(null)
    val editingMenuItem: StateFlow<MenuItemEntity?> = _editingMenuItem

    fun navigateTo(screen: ScreenState) {
        if (screen == ScreenState.ADMIN_DASHBOARD && !_isAdminAuthenticated.value) {
            _showAdminLoginDialog.value = true
        } else {
            _currentScreen.value = screen
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
    }

    fun onOriginSelect(origin: String) {
        _selectedOrigin.value = origin
    }

    fun openOrderModal(item: MenuItemEntity) {
        _selectedItemForOrder.value = item
        _orderFormState.value = OrderFormState()
    }

    fun closeOrderModal() {
        _selectedItemForOrder.value = null
    }

    fun openOrderHistoryDialog() {
        _showOrderHistoryDialog.value = true
    }

    fun closeOrderHistoryDialog() {
        _showOrderHistoryDialog.value = false
    }

    fun reorder(order: OrderEntity) {
        val matchingItem = allMenuItems.value.firstOrNull { it.name.equals(order.itemName, ignoreCase = true) }
            ?: MenuItemEntity(
                name = order.itemName,
                description = "Quick re-order from past history",
                priceGhs = if (order.quantity > 0) order.totalPriceGhs / order.quantity else order.totalPriceGhs,
                category = "Re-order",
                origin = "Both",
                drawableResName = "img_hero_banner"
            )

        _selectedItemForOrder.value = matchingItem
        _orderFormState.value = OrderFormState(
            customerName = order.customerName,
            customerPhone = order.customerPhone,
            deliveryAddress = order.deliveryAddress,
            quantity = order.quantity.coerceAtLeast(1),
            notes = order.notes
        )
        _showOrderHistoryDialog.value = false
    }

    fun updateOrderForm(name: String, phone: String, address: String, quantity: Int, notes: String) {
        _orderFormState.value = OrderFormState(
            customerName = name,
            customerPhone = phone,
            deliveryAddress = address,
            quantity = quantity.coerceAtLeast(1),
            notes = notes
        )
    }

    fun submitOrderAndOpenWhatsApp(context: Context) {
        val item = _selectedItemForOrder.value ?: return
        val form = _orderFormState.value

        if (form.customerName.isBlank() || form.customerPhone.isBlank() || form.deliveryAddress.isBlank()) {
            Toast.makeText(context, "Please fill in your Name, Phone Number, and Delivery Location.", Toast.LENGTH_LONG).show()
            return
        }

        val totalPrice = item.priceGhs * form.quantity

        // Save order to database
        viewModelScope.launch {
            val order = OrderEntity(
                customerName = form.customerName,
                customerPhone = form.customerPhone,
                deliveryAddress = form.deliveryAddress,
                itemName = item.name,
                quantity = form.quantity,
                totalPriceGhs = totalPrice,
                notes = form.notes
            )
            repository.insertOrder(order)
        }

        // Prepare WhatsApp message
        val phoneNumber = "233551464715" // International format for Ghana number 0551464715
        val message = """
            *NEW ORDER - ASEDA KITCHEN* 🍲
            --------------------------------
            *Food Item:* ${item.name}
            *Origin:* ${item.origin}
            *Quantity:* ${form.quantity}
            *Total Price:* GH₵ ${"%.2f".format(totalPrice)}
            --------------------------------
            *Customer Name:* ${form.customerName}
            *Phone Number:* ${form.customerPhone}
            *Delivery Location:* ${form.deliveryAddress}
            *Special Notes:* ${if (form.notes.isNotBlank()) form.notes else "None"}
            --------------------------------
            Please confirm availability & delivery time. Thank you!
        """.trimIndent()

        try {
            val encodedMessage = URLEncoder.encode(message, "UTF-8")
            val whatsappUri = Uri.parse("https://wa.me/$phoneNumber?text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, whatsappUri)
            context.startActivity(intent)
            closeOrderModal()
            Toast.makeText(context, "Order submitted! Opening WhatsApp to send details...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open WhatsApp: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    // Admin Auth Actions
    fun onAdminPasswordChange(password: String) {
        _adminPasswordInput.value = password
        _adminPasswordError.value = false
    }

    fun attemptAdminLogin() {
        if (repository.verifyAdminPassword(_adminPasswordInput.value)) {
            _isAdminAuthenticated.value = true
            _showAdminLoginDialog.value = false
            _adminPasswordInput.value = ""
            _adminPasswordError.value = false
            _currentScreen.value = ScreenState.ADMIN_DASHBOARD
        } else {
            _adminPasswordError.value = true
        }
    }

    fun closeAdminLoginDialog() {
        _showAdminLoginDialog.value = false
        _adminPasswordInput.value = ""
        _adminPasswordError.value = false
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
        _currentScreen.value = ScreenState.HOME
    }

    // Admin Food CRUD Actions
    fun openAddFoodDialog() {
        _editingMenuItem.value = null
        _showAddEditFoodDialog.value = true
    }

    fun openEditFoodDialog(item: MenuItemEntity) {
        _editingMenuItem.value = item
        _showAddEditFoodDialog.value = true
    }

    fun closeAddEditFoodDialog() {
        _showAddEditFoodDialog.value = false
        _editingMenuItem.value = null
    }

    fun saveMenuItem(
        name: String,
        description: String,
        priceGhs: Double,
        category: String,
        origin: String,
        drawableResName: String,
        isFeatured: Boolean
    ) {
        viewModelScope.launch {
            val current = _editingMenuItem.value
            if (current != null) {
                val updated = current.copy(
                    name = name,
                    description = description,
                    priceGhs = priceGhs,
                    category = category,
                    origin = origin,
                    drawableResName = drawableResName.ifBlank { current.drawableResName },
                    isFeatured = isFeatured
                )
                repository.updateMenuItem(updated)
            } else {
                val newItem = MenuItemEntity(
                    name = name,
                    description = description,
                    priceGhs = priceGhs,
                    category = category,
                    origin = origin,
                    drawableResName = drawableResName.ifBlank { "img_hero_banner" },
                    isFeatured = isFeatured
                )
                repository.insertMenuItem(newItem)
            }
            closeAddEditFoodDialog()
        }
    }

    fun toggleFoodAvailability(item: MenuItemEntity) {
        viewModelScope.launch {
            repository.updateMenuItem(item.copy(isAvailable = !item.isAvailable))
        }
    }

    fun deleteMenuItem(item: MenuItemEntity) {
        viewModelScope.launch {
            repository.deleteMenuItem(item)
        }
    }

    fun deleteOrder(orderId: Int) {
        viewModelScope.launch {
            repository.deleteOrder(orderId)
        }
    }
}

class AsedaViewModelFactory(private val repository: AsedaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AsedaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AsedaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
