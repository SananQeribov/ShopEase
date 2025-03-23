package com.example.shopease.ui.theme

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shopease.ui.theme.screen.home.HomeScreen
import com.example.shopease.utils.BottomNavItems
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.shopease.ShopperSession
import com.example.shopease.model.UiProductModel
import com.example.shopease.navigation.CartScreen
import com.example.shopease.navigation.HomeScreen
import com.example.shopease.navigation.LoginScreen
import com.example.shopease.navigation.ProductDetails
import com.example.shopease.navigation.ProfileScreen
import com.example.shopease.navigation.RegisterScreen
import com.example.shopease.navigation.productNavType
import com.example.shopease.ui.theme.screen.account.login.LoginScreen
import com.example.shopease.ui.theme.screen.account.register.RegisterScreen
import com.example.shopease.ui.theme.screen.cart.CardBasketScreen
import com.example.shopease.ui.theme.screen.product_detail.ProductDetailScreen
import com.example.shopease.viewModel.LogoViewModel
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<LogoViewModel>()
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
            .apply {
                setKeepOnScreenCondition {
                    viewModel.isLoading.value
                }
            }
        setContent {
            ShopEaseTheme {
                val shouldShowBottomNav = remember {
                    mutableStateOf(true)
                }
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedVisibility(visible = shouldShowBottomNav.value, enter = fadeIn()) {
                            BottomNavigationBar(navController)
                        }
                    }
                ){
                    NavHost(
                        navController = navController,
                        startDestination = if (ShopperSession.getUser() != null) {
                            HomeScreen
                        } else {
                            LoginScreen
                        }
                    ) {
                        composable<LoginScreen> {
                            shouldShowBottomNav.value = false
                            LoginScreen(navController)
                        }
                        composable<RegisterScreen> {
                            shouldShowBottomNav.value = false
                            RegisterScreen(navController)
                        }
                        composable<HomeScreen> { HomeScreen(navController)
                            shouldShowBottomNav.value = true
                        }
                        composable<CartScreen> { CardBasketScreen(navController)

                            shouldShowBottomNav.value = true
                        }
                        composable<ProfileScreen> { Box(modifier = Modifier.fillMaxSize()){
                            Text(text = "Profile")
                            shouldShowBottomNav.value = true
                        }

                        }
                        composable<ProductDetails>(
                            typeMap = mapOf(typeOf<UiProductModel>() to productNavType)
                        ) {
                            shouldShowBottomNav.value = false
                            val productRoute = it.toRoute<ProductDetails>()
                            Box(modifier = Modifier.fillMaxSize()) {
                                ProductDetailScreen(navController, productRoute.product)
                            }
                        }
                    }

                }

                }
            }
        }
    @Composable
    fun BottomNavigationBar(navController: NavController) {
        NavigationBar (
                containerColor = Color.Black.copy(0.3f))
                {
            //current route
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            val items = listOf(
                BottomNavItems.Home,
                BottomNavItems.Cart,
                BottomNavItems.Profile
            )

            items.forEach { item ->
                val isSelected =currentRoute?.substringBefore("?") == item.route::class.qualifiedName
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { startRoute ->
                                popUpTo(startRoute) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text(text = item.title) },
                    icon = {
                        Image(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(if(isSelected) MaterialTheme.colorScheme.primary else Color.Gray)
                        )
                    }, colors = NavigationBarItemDefaults.colors().copy(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = Color.Gray,
                        unselectedIconColor = Color.Gray
                    )
                )
            }
        }
    }

}


