package com.example.shopease.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Product
import com.example.domain.model.request.AddCartRequestModel
import com.example.domain.usercase.CartUserCase
import com.example.domain.util.ResultWrapper
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


    private val _basketUi = MutableStateFlow<BasketScreenUIEvents>(BasketScreenUIEvents.Initial)
    val basketUi = _basketUi.asStateFlow()

    private val _products = MutableLiveData<List<UiProductModel>>(emptyList())
    val products: LiveData<List<UiProductModel>> = _products


    fun removeBasket(product: UiProductModel) {

        val currentList = _products.value?.toMutableList() ?: mutableListOf()

        val updatedList = currentList.map {
            if (it.id == product.id) {

                it.copy(count = it.count - 1)
            } else {
                it
            }
        }.toMutableList()


        val finalList = updatedList.filter { it.count > 0 }.toMutableList()


        _products.value = finalList

        _basketUi.value = BasketScreenUIEvents.Success(finalList)
    }


    fun addBasket(product: UiProductModel) {
        val currentList = _products.value?.toMutableList() ?: mutableListOf()


        val updatedList = currentList.map {
            if (it.id == product.id) {

                it.copy(count = it.count + 1)
            } else {
                it
            }
        }.toMutableList()


        if (updatedList.none { it.id == product.id }) {
            updatedList.add(product.copy(count = 1))
        }
        _products.value = updatedList

        _basketUi.value = BasketScreenUIEvents.Success(updatedList)
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
                is ResultWrapper.Success -> {
                    _state.value = ProductDetailsEvent.Success("Product added to cart")
                    Log.e("bs","${_state.value}")
                }

                is ResultWrapper.Failure -> {

                    _state.value = ProductDetailsEvent.Error("Something went wrong!")
                    Log.e("bs","${_state.value}")
                }

                is ResultWrapper.Failure -> TODO()
                is ResultWrapper.Success -> TODO()
            }
        }
    }


}

