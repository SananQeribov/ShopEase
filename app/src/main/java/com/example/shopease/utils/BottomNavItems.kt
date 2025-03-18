package com.example.shopease.utils

import com.example.shopease.R
import com.example.shopease.navigation.CartScreen
import com.example.shopease.navigation.HomeScreen
import com.example.shopease.navigation.ProfileScreen

sealed class BottomNavItems(val route: Any, val title: String, val icon: Int) {
    object Home : BottomNavItems(HomeScreen, "Home", icon = R.drawable.ic_home)
    object Cart : BottomNavItems(CartScreen, "Cart", icon = R.drawable.ic_cart)
    object Profile : BottomNavItems(ProfileScreen, "Profile", icon = R.drawable.profile)
}