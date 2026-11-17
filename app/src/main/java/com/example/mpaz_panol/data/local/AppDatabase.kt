package com.example.mpaz_panol.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mpaz_panol.data.local.dao.*
import com.example.mpaz_panol.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UserEntity::class, ItemEntity::class, RequestEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun itemDao(): ItemDao
    abstract fun requestDao(): RequestDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mpaz_panol_db"
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
                        populateInitialData(database.userDao(), database.itemDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(userDao: UserDao, itemDao: ItemDao) {
            // Usuarios de prueba (Rol automático por dominio de correo)
            userDao.insertUser(
                UserEntity(
                    fullName = "Encargado TI Pañol",
                    rut = "11.111.111-1",
                    email = "admin@panol.mpaz.cl",
                    password = "123456",
                    role = "ADMIN"
                )
            )
            userDao.insertUser(
                UserEntity(
                    fullName = "Prof. Camila Rojas",
                    rut = "22.222.222-2",
                    email = "camila@docente.mpaz.cl",
                    password = "123456",
                    role = "SOLICITANTE"
                )
            )

            // Catálogo inicial ficticio
            val items = listOf(
                ItemEntity(name = "Proyector Epson PowerLite X49", description = "3600 lúmenes, HDMI/VGA", category = "Proyector", totalStock = 5, availableStock = 5, status = "Disponible", location = "Estante A1 - Pañol Central"),
                ItemEntity(name = "Notebook Lenovo ThinkPad E14", description = "Core i5, 16GB RAM, Windows 11", category = "Notebook", totalStock = 10, availableStock = 10, status = "Disponible", location = "Carro Carga B2"),
                ItemEntity(name = "Kit Robótica LEGO WeDo 2.0", description = "Set completo con sensores y motor", category = "Robótica", totalStock = 6, availableStock = 6, status = "Disponible", location = "Gabinete Robótica C1"),
                ItemEntity(name = "Placa Microcontrolador ESP32", description = "WiFi + Bluetooth + Cable USB", category = "Robótica", totalStock = 15, availableStock = 15, status = "Disponible", location = "Caja Plástica C2"),
                ItemEntity(name = "Parlante Activo JBL EON", description = "Incluye trípode y micrófono alámbrico", category = "Parlante", totalStock = 3, availableStock = 3, status = "Disponible", location = "Zona Audio D1"),
                ItemEntity(name = "Alargador Zapatilla 6 Tomas 10m", description = "Certificación SEC uso pesado", category = "Alargador", totalStock = 12, availableStock = 12, status = "Disponible", location = "Perchero E1"),
                ItemEntity(name = "Switch Cisco Catalyst 2960", description = "24 puertos Gigabit (Infraestructura)", category = "Red", totalStock = 2, availableStock = 2, status = "Disponible", location = "Rack Sala Profesores / Pañol")
            )
            items.forEach { itemDao.insertItem(it) }
        }
    }
}