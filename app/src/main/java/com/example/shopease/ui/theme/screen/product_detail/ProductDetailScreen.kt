package com.example.shopease.ui.theme.screen.product_detail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
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
import com.example.shopease.R
import com.example.shopease.model.UiProductModel
import com.example.shopease.utils.ProductDetailsEvent
import com.example.shopease.viewModel.ProductDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailScreen(
    navController: NavController,
    productModel: UiProductModel,
    viewModel: ProductDetailViewModel = koinViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. **Şəkil yuxarıda yerləşir**
        AsyncImage(
            model = productModel.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp) // Şəkilin hündürlüyü
        )

        // 2. **Başlıq və Qiymət**
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = productModel.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$${productModel.price}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // 3. **Reytinq və Rəylər**
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = R.drawable.ic_star), contentDescription = null)
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "4.5",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "(10 Reviews)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        // 4. **Təsvir**
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Description",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Gray
        )
        Text(
            text = productModel.description,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            color = Color.Gray
        )

        // 5. **Boşluq, ölçü seçimlərini aşağı itələyir**
        Spacer(modifier = Modifier.weight(1f))

        // 6. **Ölçü seçimləri aşağıdadır**
        Text(text = "Size", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(4) {
                SizeItem(size = "${it + 1}", isSelected = 0 == it) { }
            }
        }

        // 7. **Düymələr ən aşağıdadır**
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.addBasket(productModel) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Buy Now")
            }
            IconButton(
                onClick = { viewModel.addBasket(productModel) },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.4f))
            ) {
                Image(painter = painterResource(id = R.drawable.ic_cart), contentDescription = null)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        val detailState = viewModel.state.collectAsState()
        val loading = rememberSaveable {
            mutableStateOf(false)
        }
LaunchedEffect (detailState.value ){
        when (detailState.value) {

            is ProductDetailsEvent.Success -> {
                Log.e("ProductDetailScreen", "Success: ${detailState.value}")
                Toast.makeText(
                    navController.context,
                    (detailState.value as ProductDetailsEvent.Success).message,
                    Toast.LENGTH_LONG
                ).show()
                loading.value = false

            }

            is ProductDetailsEvent.Error -> {
                Toast.makeText(
                    navController.context,
                    (detailState.value as ProductDetailsEvent.Error).message,
                    Toast.LENGTH_LONG
                ).show()
                loading.value = false
            }

            ProductDetailsEvent.Loading -> {
                loading.value = true
            }


            ProductDetailsEvent.Nothing -> {
                loading.value = false
            }
        }
        }
        if (loading.value) {
            Column(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Adding to  cart",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary

                )


            }

        }

    }



}



// **Ölçü qutusu komponenti**
@Composable
fun SizeItem(size: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = size,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}
