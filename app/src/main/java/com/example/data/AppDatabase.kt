package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [MenuItemEntity::class, OrderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aseda_kitchen_database"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialDatabase(database.menuItemDao())
                    }
                }
            }
        }

        suspend fun populateInitialDatabase(dao: MenuItemDao) {
            if (dao.getCount() == 0) {
                val initialItems = listOf(
                    MenuItemEntity(
                        name = "Banku with Tilapia & Hot Pepper",
                        description = "Freshly molded Banku served hot with whole grilled seasoned Tilapia, fiery raw red pepper salsa, diced onions & rich black Shito sauce.",
                        priceGhs = 65.00,
                        category = "Ghanaian Specialties",
                        origin = "Ghanaian",
                        drawableResName = "img_banku_tilapia",
                        isFeatured = true
                    ),
                    MenuItemEntity(
                        name = "Fufu with Goat Meat Light Soup",
                        description = "Silky smooth cassava & plantain Fufu served in aromatic, spicy tomato light soup loaded with tender goat meat and habanero peppers.",
                        priceGhs = 75.00,
                        category = "Ghanaian Specialties",
                        origin = "Ghanaian",
                        drawableResName = "img_fufu_soup",
                        isFeatured = true
                    ),
                    MenuItemEntity(
                        name = "Nigerian Egusi Soup & Pounded Yam",
                        description = "Rich ground melon seed soup simmered with spinach, stockfish, cow skin (kpomo) & assorted meats, paired with fluffy Pounded Yam.",
                        priceGhs = 80.00,
                        category = "Nigerian Favorites",
                        origin = "Nigerian",
                        drawableResName = "img_egusi_pounded_yam",
                        isFeatured = true
                    ),
                    MenuItemEntity(
                        name = "Waakye Supreme Special",
                        description = "Classic Ghanaian rice & black-eyed beans cooked with sorghum leaves. Served with fried plantains (tatale), boiled egg, wele, gari fortor & dark spicy Shito.",
                        priceGhs = 55.00,
                        category = "Ghanaian Specialties",
                        origin = "Ghanaian",
                        drawableResName = "img_hero_banner",
                        isFeatured = true
                    ),
                    MenuItemEntity(
                        name = "Aseda Special Jollof Rice",
                        description = "Smoky long-grain Jollof rice cooked in rich spiced tomato stew, accompanied by crispy fried chicken, fried plantains & fresh coleslaw.",
                        priceGhs = 50.00,
                        category = "Rice & Swallows",
                        origin = "Both",
                        drawableResName = "img_hero_banner",
                        isFeatured = true
                    ),
                    MenuItemEntity(
                        name = "Konkonte (Abetee) & Groundnut Soup",
                        description = "Hearty dried cassava flour swallow (Face the Wall) paired with creamy peanut groundnut soup, dried smoked fish & beef.",
                        priceGhs = 45.00,
                        category = "Ghanaian Specialties",
                        origin = "Ghanaian",
                        drawableResName = "img_fufu_soup",
                        isFeatured = false
                    ),
                    MenuItemEntity(
                        name = "Fried Yam & Turkey Wings",
                        description = "Golden crispy fried Pona yam chips served with seasoned grilled turkey wings, green pepper sauce & hot pepper shito.",
                        priceGhs = 60.00,
                        category = "Grills & Soups",
                        origin = "Ghanaian",
                        drawableResName = "img_banku_tilapia",
                        isFeatured = false
                    ),
                    MenuItemEntity(
                        name = "Nigerian Suya Skewers (Spicy Beef)",
                        description = "Thinly sliced tender beef marinated in peanut yaji spice, flame-grilled over open coals, served with fresh red onions & extra yaji pepper.",
                        priceGhs = 45.00,
                        category = "Nigerian Favorites",
                        origin = "Nigerian",
                        drawableResName = "img_hero_banner",
                        isFeatured = true
                    ),
                    MenuItemEntity(
                        name = "Goat Meat Pepper Soup",
                        description = "Spicy herbal Nigerian pepper soup broth simmered with tender cutlets of goat meat, scent leaves, and traditional African soup spices.",
                        priceGhs = 50.00,
                        category = "Grills & Soups",
                        origin = "Nigerian",
                        drawableResName = "img_egusi_pounded_yam",
                        isFeatured = false
                    ),
                    MenuItemEntity(
                        name = "Efo Riro with Garri (Eba)",
                        description = "Rich Yoruba-style savory spinach stew cooked with locust beans (iru), dried fish & smoked turkey, served with warm yellow Garri swallow.",
                        priceGhs = 70.00,
                        category = "Nigerian Favorites",
                        origin = "Nigerian",
                        drawableResName = "img_egusi_pounded_yam",
                        isFeatured = false
                    ),
                    MenuItemEntity(
                        name = "Chilled Sobolo / Zobo Juice",
                        description = "Refreshing natural dried hibiscus flower drink infused with ginger, pineapple, cloves & fresh mint leaves.",
                        priceGhs = 15.00,
                        category = "Drinks",
                        origin = "Both",
                        drawableResName = "img_hero_banner",
                        isFeatured = false
                    )
                )
                dao.insertAll(initialItems)
            }
        }
    }
}
