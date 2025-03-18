package com.example.shopease.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Product
import com.example.domain.model.request.AddCartRequestModel
import com.example.domain.usercase.CartUserCase
import com.example.shopease.model.UiProductModel
import com.example.shopease.utils.BasketScreenUIEvents
import com.example.shopease.utils.HomeScreenUIEvents
import com.example.shopease.utils.ProductDetailsEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(val usaCase:CartUserCase):ViewModel() {
    private val _state = MutableStateFlow<ProductDetailsEvent>(ProductDetailsEvent.Nothing)
    val state = _state.asStateFlow()


    private val _products = MutableStateFlow<List<UiProductModel>>(emptyList())
    val products = _products.asStateFlow()

    private val _basketUi = MutableStateFlow<BasketScreenUIEvents>(BasketScreenUIEvents.Loading)
    val basketUi = _basketUi.asStateFlow()



    private fun addBasket(product: UiProductModel) {
        viewModelScope.launch {
            _basketUi.value = BasketScreenUIEvents.Loading

            var currentList = products.value.toMutableList()
            currentList.add(product)

            _basketUi.value = BasketScreenUIEvents.Success(currentList.toList())






        }
    }

    var _basket = MutableStateFlow<ProductDetailsEvent>(ProductDetailsEvent.Nothing)
    val basket = _state.asStateFlow()
    fun addProductToCart(product: UiProductModel) {
        viewModelScope.launch {
            _state.value = ProductDetailsEvent.Loading
            val result = usaCase.execute(
                AddCartRequestModel(
                    product.id,
                    product.title,
                    product.price,
                    1,
                    1
                )
            )
            when (result) {
                is com.example.domain.util.ResultWrapper.Success -> {
                    _state.value = ProductDetailsEvent.Success("Product added to cart")
                    Log.e("bs","${_state.value}")
                }

                is com.example.domain.util.ResultWrapper.Failure -> {

                    _state.value = ProductDetailsEvent.Error("Something went wrong!")
                    Log.e("bs","${_state.value}")
                }
            }
        }
    }


}

