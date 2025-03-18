package com.example.shopease.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Product
import com.example.domain.usercase.CategoryUseCase
import com.example.domain.usercase.ProductUseCase
import com.example.domain.util.ResultWrapper
import com.example.shopease.utils.HomeScreenUIEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class HomeViewModel(
    private val productUseCase: ProductUseCase,
    private val categoryUseCase: CategoryUseCase
) : ViewModel() {

    private val _homeUi = MutableStateFlow<HomeScreenUIEvents>(HomeScreenUIEvents.Loading)
    val homeUi = _homeUi.asStateFlow()

    init {
        getAllData()
    }

    private fun getAllData() {
        viewModelScope.launch {
            _homeUi.value = HomeScreenUIEvents.Loading

            // Məhsulları və kateqoriyaları yüklə
            val featured = getProducts("electronics")
            val popularProducts = getProducts("jewelery")
            val categories = getCategory()

            // Yükləmə nəticələrini yoxla
            if (categories != null) {
                if (featured.isEmpty() && popularProducts.isEmpty() && categories.isNotEmpty()) {
                    _homeUi.value = HomeScreenUIEvents.Error("Failed to load products")
                    return@launch
                }
            }

            // Uğurlu nəticəni təqdim et
            _homeUi.value = HomeScreenUIEvents.Success(featured, popularProducts, categories)
        }
    }

    private suspend fun getProducts(category: String?): List<Product> {
        productUseCase.execute(category).let { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    return (result).value
                }

                is ResultWrapper.Failure -> {
                    return emptyList()
                }
            }
        }
    }

    private suspend fun getCategory(): List<String> {
        return when (val result = categoryUseCase.execute()) {
            is ResultWrapper.Success -> {
                result.value // Kateqoriyalar uğurla yükləndi
            }
            is ResultWrapper.Failure -> {
                emptyList() // Kateqoriyalar yüklənmədikdə boş siyahı qaytar
            }
        }
    }
}
