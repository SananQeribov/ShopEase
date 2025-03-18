package com.example.shopease.ui.theme.screen.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.domain.model.Product
import com.example.shopease.R
import com.example.shopease.model.UiProductModel
import com.example.shopease.navigation.ProductDetails
import com.example.shopease.utils.HomeScreenUIEvents
import com.example.shopease.viewModel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    val homeScreen = viewModel.homeUi.collectAsState()

    val loading = rememberSaveable  {
        mutableStateOf(false)
    }
    val errorMessage = rememberSaveable {
        mutableStateOf<String?>(null)
    }
    val feature = rememberSaveable {
        mutableStateOf<List<Product>>(emptyList())
    }
    val popular =rememberSaveable {
        mutableStateOf<List<Product>>(emptyList())
    }
    val categories = rememberSaveable {
        mutableStateOf<List<String>>(emptyList())
    }
    Scaffold {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), color = MaterialTheme.colorScheme.primary
        ) {
            when (homeScreen.value) {
                is HomeScreenUIEvents.Error -> {
                    val data = (homeScreen.value as HomeScreenUIEvents.Success)
                    loading.value = false
                    errorMessage.value =data.toString()
                      //  Text(text = (homeScreen.value as HomeScreenUIEvents.Error).message)
                    Log.e("sn", homeScreen.value.toString())
                }

                HomeScreenUIEvents.Loading -> {
                    loading.value = true
                    errorMessage.value = null
                }

                is HomeScreenUIEvents.Success -> {
                    val data = (homeScreen.value as HomeScreenUIEvents.Success)
                    feature.value = data.featured
                    popular.value = data.popularProduct
                    categories.value = data.categories
                    loading.value = false
                    errorMessage.value = null

                }
            }
            ProductList (
                feature.value, popular.value, categories.value, loading.value, errorMessage.value, onClick = {
                    navController.navigate(ProductDetails(UiProductModel.fromProduct(it)))
                })
        }
    }


}

@Composable
fun SearchBar(value: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onTextChanged,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f)

        ),
        placeholder = {
            Text(
                text = "Search for products",
                style = MaterialTheme.typography.bodySmall
            )

        }


    )


}

@Composable
fun ProfilePart() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Matilda Brown", fontWeight = FontWeight.SemiBold

                )
                Text(
                    text = "matildabrown@mail.com", style = MaterialTheme.typography.titleSmall

                )

            }

        }

        Image(
            painter = painterResource(id = R.drawable.notification),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.3f))
                .padding(8.dp),
            contentScale = ContentScale.Inside
        )

    }


}

@Composable

fun ProductList(
    featured: List<Product>,
    popularProducts: List<Product>,
    category: List<String>,
    isLoading: Boolean = false,
    isError: String? = null,onClick: (Product) -> Unit
) {
    LazyColumn {
        item {
            ProfilePart()
            Spacer(modifier = Modifier.size(16.dp))
            SearchBar(value = "", onTextChanged = {})
            Spacer(modifier = Modifier.size(10.dp))
        }
        item {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp) , color = Color.White)
                    Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
                }
            }
            isError?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
        }
        item {
            if (category.isNotEmpty()) {
                Spacer(modifier = Modifier.padding(16.dp))
                LazyRow {
                    items(category, key = { it }) { category ->
                        val isVisible = remember {
                            mutableStateOf(false)
                        }
                        LaunchedEffect(true) {
                            isVisible.value = true
                        }
                        AnimatedVisibility(
                            visible = isVisible.value, enter = fadeIn() + expandVertically()
                        ){
                        Text(
                            text = category.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(8.dp)
                        )
                    }

                }

            }}
            Spacer(modifier = Modifier.padding(16.dp))
        }
        item {
            if (featured.isNotEmpty()) {
                ProductRow(product = featured, title = "Featured", onClick = onClick)
                Spacer(modifier = Modifier.padding(16.dp))
            }
            if (popularProducts.isNotEmpty()) {
                ProductRow(product = popularProducts, title = "Popular Products", onClick = onClick)
            }
        }

    }

}

@Composable
fun ProductItem(product: Product,onClick:(Product)->Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(width = 126.dp, height = 154.dp).clickable{onClick(product)},
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(contentColor = Color.Blue.copy(alpha = 0.3f))
    ) {


        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = product.title,
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }


}


@Composable
fun ProductRow(product: List<Product>, title: String,onClick: (Product) -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Text(
                text = "See all",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        LazyRow {
            items(product) { product ->
                ProductItem(product = product, onClick)
            }
        }


    }
}