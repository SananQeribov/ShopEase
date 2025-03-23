package com.example.shopease

import android.content.Context
import com.example.domain.model.UserModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ShopperSession : KoinComponent {
    private val context: Context by inject()

    fun storeUser(user: UserModel) {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("id", user.id!!)
            putString("username", user.username)
            putString("email", user.email)
            putString("password", user.password)
            apply()
        }
    }

    fun getUser(): UserModel? {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val id = sharedPref.getInt("id", 0)
        val username = sharedPref.getString("username", null)
        val email = sharedPref.getString("email", null)
        val password = sharedPref.getString("password", null)
        return if (id != 0 && username != null && email != null &&password != null) {
            UserModel(id, username, email, password)
        } else {
            null
        }
    }
}