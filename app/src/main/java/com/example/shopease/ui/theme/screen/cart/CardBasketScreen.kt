package com.example.shopease.ui.theme.screen.cart

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopease.R

import com.example.shopease.model.UiProductModel
import com.example.shopease.utils.BasketScreenUIEvents
import com.example.shopease.utils.CartEvent
import com.example.shopease.viewModel.ProductDetailViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardBasketScreen(navController: NavController, viewModel: ProductDetailViewModel = koinViewModel()) {

    val uiState = viewModel.basketUi.collectAsState()
    val cartItems = remember {
        mutableStateOf(emptyList<UiProductModel>())
    }
    val cardElements = viewModel.products.value
    Log.e("data is cards","${cardElements}")

    val loading = remember {
        mutableStateOf(false)
    }
    val errorMsg = remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is BasketScreenUIEvents.Loading -> {
                loading.value = true
                errorMsg.value = null
            }

            is BasketScreenUIEvents.Error -> {
                loading.value = false
                errorMsg.value = (uiState.value as BasketScreenUIEvents.Error).message
            }

            is BasketScreenUIEvents.Success -> {
                loading.value = false
                val data = (uiState.value as BasketScreenUIEvents.Success).cards
                if (data.isEmpty()) {
                    Log.e("data", "$data")
                    errorMsg.value = "No items in cart"
                } else {
                    cartItems.value = data
                    Log.e("data", "$data")
                }
            }

            BasketScreenUIEvents.Initial -> {
                loading.value = false
            }
        }
    }

    // Hesablamalar
    val totalCount = cartItems.value.sumOf { it.count }
    val totalPrice = cartItems.value.sumOf { it.count * it.price }

    Column(modifier = Modifier.fillMaxSize()) {
        val pullToRefreshState = rememberPullToRefreshState()
        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                viewModel.products
                delay(500)
                pullToRefreshState.endRefresh()
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            PullToRefreshContainer(
                state = pullToRefreshState, modifier = Modifier.align(Alignment.TopCenter)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .nestedScroll(pullToRefreshState.nestedScrollConnection)
                    .padding(16.dp)

            ) {
                Text(text = "Cart", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.size(8.dp))

                val shouldShowList = !loading.value && errorMsg.value == null
                AnimatedVisibility(
                    visible = shouldShowList, enter = fadeIn(), modifier = Modifier.weight(1f)
                ) {
                    LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        items(cartItems.value) { item ->
                            CartItem(
                                item = item,
                                onIncrease = { viewModel.addBasket(item) },
                                onDecrease = { viewModel.removeBasket(item) }
                            )
                        }
                    }
                }

                if (shouldShowList) {
                    // Umumi statistika
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 46.dp),
                            text = "Total Count: $totalCount",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Total Price: $${"%.2f".format(totalPrice)}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth().padding(bottom = 65.dp)) {
                        Text(text = "Checkout")
                    }
                }
            }

            // Loading və error mesajları
            if (loading.value) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    Text(text = "Loading...")
                }
            }
            if (errorMsg.value != null) {
                Text(
                    text = errorMsg.value ?: "Something went wrong!",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@Composable
fun CartItem(item: UiProductModel, onIncrease: (UiProductModel) -> Unit, onDecrease: (UiProductModel) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray.copy(alpha = 0.4f))
    ) {
        // Məhsul şəkli
        AsyncImage(
            model = item.image,
            contentDescription = null,
            modifier = Modifier.size(126.dp, 96.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.size(8.dp))

        // Məhsulun məlumatları
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 2,  // Maksimum bir sətir göstərilsin
                overflow = TextOverflow.Ellipsis  // Uzun mətnlər üçün üç nöqtə əlavə et
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "$${item.price}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.size(4.dp))

            // Məhsul sayını göstərmək
            Text(
                text = "Count: ${item.count}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal),
                color = Color.Gray
            )
        }

        // Sayını artırmaq və azaltmaq üçün düymələr
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp)
        ) {
            Row {
                // Azaltma düyməsi
                IconButton(onClick = { onDecrease(item) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Decrease",
                        modifier = Modifier.size(24.dp)
                    )
                }
                // Artırma düyməsi
                IconButton(onClick = { onIncrease(item) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}



