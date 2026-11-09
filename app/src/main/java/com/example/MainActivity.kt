package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.AsedaRepository
import com.example.ui.AsedaViewModel
import com.example.ui.AsedaViewModelFactory
import com.example.ui.ScreenState
import com.example.ui.components.AsedaBottomNav
import com.example.ui.components.AsedaTopBar
import com.example.ui.dialogs.AddEditFoodDialog
import com.example.ui.dialogs.AdminLoginDialog
import com.example.ui.dialogs.OrderHistoryDialog
import com.example.ui.dialogs.OrderModalDialog
import com.example.ui.screens.AboutUsScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.ChefAiChatScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.theme.AsedaKitchenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            val database = AppDatabase.getDatabase(context)
            val repository = AsedaRepository(database.menuItemDao(), database.orderDao())
            val viewModel: AsedaViewModel = viewModel(factory = AsedaViewModelFactory(repository))

            AsedaKitchenApp(viewModel = viewModel)
        }
    }
}

@Composable
fun AsedaKitchenApp(viewModel: AsedaViewModel) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedOrigin by viewModel.selectedOrigin.collectAsStateWithLifecycle()

    val featuredItems by viewModel.featuredMenuItems.collectAsStateWithLifecycle()
    val filteredMenuItems by viewModel.filteredMenuItems.collectAsStateWithLifecycle()
    val allMenuItems by viewModel.allMenuItems.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()

    val selectedItemForOrder by viewModel.selectedItemForOrder.collectAsStateWithLifecycle()
    val orderFormState by viewModel.orderFormState.collectAsStateWithLifecycle()
    val showOrderHistoryDialog by viewModel.showOrderHistoryDialog.collectAsStateWithLifecycle()

    val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsStateWithLifecycle()
    val showAdminLoginDialog by viewModel.showAdminLoginDialog.collectAsStateWithLifecycle()
    val adminPasswordInput by viewModel.adminPasswordInput.collectAsStateWithLifecycle()
    val adminPasswordError by viewModel.adminPasswordError.collectAsStateWithLifecycle()

    val showAddEditFoodDialog by viewModel.showAddEditFoodDialog.collectAsStateWithLifecycle()
    val editingMenuItem by viewModel.editingMenuItem.collectAsStateWithLifecycle()

    // Handle Back button for non-home screens
    if (currentScreen != ScreenState.HOME) {
        BackHandler {
            viewModel.navigateTo(ScreenState.HOME)
        }
    }

    AsedaKitchenTheme {
        Scaffold(
            topBar = {
                AsedaTopBar(
                    currentScreen = currentScreen,
                    isAdminAuthenticated = isAdminAuthenticated,
                    orderCount = allOrders.size,
                    onOrderHistoryClick = {
                        viewModel.openOrderHistoryDialog()
                    },
                    onAdminClick = {
                        viewModel.navigateTo(ScreenState.ADMIN_DASHBOARD)
                    }
                )
            },
            bottomBar = {
                AsedaBottomNav(
                    currentScreen = currentScreen,
                    onScreenSelected = { screen ->
                        viewModel.navigateTo(screen)
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    ScreenState.HOME -> {
                        HomeScreen(
                            featuredItems = featuredItems,
                            onExploreMenuClick = { viewModel.navigateTo(ScreenState.MENU) },
                            onChefAiClick = { viewModel.navigateTo(ScreenState.CHEF_AI) },
                            onOrderClick = { item -> viewModel.openOrderModal(item) }
                        )
                    }

                    ScreenState.MENU -> {
                        MenuScreen(
                            menuItems = filteredMenuItems,
                            searchQuery = searchQuery,
                            selectedCategory = selectedCategory,
                            selectedOrigin = selectedOrigin,
                            onSearchChange = { query -> viewModel.onSearchQueryChange(query) },
                            onCategoryChange = { cat -> viewModel.onCategorySelect(cat) },
                            onOriginChange = { origin -> viewModel.onOriginSelect(origin) },
                            onOrderClick = { item -> viewModel.openOrderModal(item) }
                        )
                    }

                    ScreenState.CHEF_AI -> {
                        ChefAiChatScreen(
                            onExploreMenuClick = { viewModel.navigateTo(ScreenState.MENU) }
                        )
                    }

                    ScreenState.ABOUT -> {
                        AboutUsScreen(
                            onExploreMenuClick = { viewModel.navigateTo(ScreenState.MENU) }
                        )
                    }

                    ScreenState.CONTACT -> {
                        ContactScreen()
                    }

                    ScreenState.ADMIN_DASHBOARD -> {
                        AdminDashboardScreen(
                            menuItems = allMenuItems,
                            orders = allOrders,
                            onAddFoodClick = { viewModel.openAddFoodDialog() },
                            onEditFoodClick = { item -> viewModel.openEditFoodDialog(item) },
                            onToggleAvailability = { item -> viewModel.toggleFoodAvailability(item) },
                            onDeleteFoodClick = { item -> viewModel.deleteMenuItem(item) },
                            onDeleteOrderClick = { orderId -> viewModel.deleteOrder(orderId) },
                            onLogoutClick = { viewModel.logoutAdmin() }
                        )
                    }
                }
            }

            // Dialogs
            selectedItemForOrder?.let { item ->
                OrderModalDialog(
                    item = item,
                    formState = orderFormState,
                    onFormUpdate = { name, phone, address, qty, notes ->
                        viewModel.updateOrderForm(name, phone, address, qty, notes)
                    },
                    onSubmit = {
                        viewModel.submitOrderAndOpenWhatsApp(context)
                    },
                    onDismiss = {
                        viewModel.closeOrderModal()
                    }
                )
            }

            if (showOrderHistoryDialog) {
                OrderHistoryDialog(
                    orders = allOrders,
                    onReorderClick = { order ->
                        viewModel.reorder(order)
                    },
                    onDeleteOrderClick = { orderId ->
                        viewModel.deleteOrder(orderId)
                    },
                    onDismiss = {
                        viewModel.closeOrderHistoryDialog()
                    }
                )
            }

            if (showAdminLoginDialog) {
                AdminLoginDialog(
                    passwordInput = adminPasswordInput,
                    isError = adminPasswordError,
                    onPasswordChange = { pwd -> viewModel.onAdminPasswordChange(pwd) },
                    onLoginSubmit = { viewModel.attemptAdminLogin() },
                    onDismiss = { viewModel.closeAdminLoginDialog() }
                )
            }

            if (showAddEditFoodDialog) {
                AddEditFoodDialog(
                    editingItem = editingMenuItem,
                    onSave = { name, desc, price, cat, origin, res, featured ->
                        viewModel.saveMenuItem(name, desc, price, cat, origin, res, featured)
                    },
                    onDismiss = { viewModel.closeAddEditFoodDialog() }
                )
            }
        }
    }
}
