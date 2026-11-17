package com.example.mpaz_panol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mpaz_panol.data.local.AppDatabase
import com.example.mpaz_panol.data.local.SessionManager
import com.example.mpaz_panol.data.repository.InventoryRepository
import com.example.mpaz_panol.data.repository.RequestRepository
import com.example.mpaz_panol.data.repository.UserRepository
import com.example.mpaz_panol.ui.auth.AuthViewModel
import com.example.mpaz_panol.ui.inventory.InventoryViewModel
import com.example.mpaz_panol.ui.navigation.AppNavigation
import com.example.mpaz_panol.ui.request.RequestViewModel
import com.example.mpaz_panol.ui.theme.MpazPanolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val sessionManager = SessionManager(this)

        val userRepo = UserRepository(db.userDao())
        val inventoryRepo = InventoryRepository(db.itemDao())
        val requestRepo = RequestRepository(db.requestDao(), db.itemDao())

        val authViewModel = AuthViewModel(userRepo, sessionManager)
        val inventoryViewModel = InventoryViewModel(inventoryRepo)
        val requestViewModel = RequestViewModel(requestRepo)

        setContent {
            MpazPanolTheme {
                AppNavigation(
                    authViewModel = authViewModel,
                    inventoryViewModel = inventoryViewModel,
                    requestViewModel = requestViewModel
                )
            }
        }
    }
}