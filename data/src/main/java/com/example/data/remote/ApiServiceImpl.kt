package com.example.data.remote

import android.util.Log
import com.example.data.model.CartResponse
import com.example.data.model.ProductModel
import com.example.data.model.request.AddToCartRequest
import com.example.data.model.request.LoginRequest
import com.example.data.model.request.RegisterRequest
import com.example.data.model.response.UserResponse
import com.example.domain.model.CartModel
import com.example.domain.model.Product
import com.example.domain.model.UserModel
import com.example.domain.model.request.AddCartRequestModel
import com.example.domain.remote.ApiService
import com.example.domain.util.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import java.io.IOException
import java.util.concurrent.TimeoutException


class ApiServiceImpl(val client: HttpClient) : ApiService {
    private val baseUrl = "https://fakestoreapi.com"
    override suspend fun getProducts(category: String?): ResultWrapper<List<Product>> {
        val url =
            if (category != null) "$baseUrl/products/category/$category" else "$baseUrl/products"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { dataModels: List<ProductModel> ->
                dataModels.map { it.toProduct() }
            })

    }

    override suspend fun getCategories(): ResultWrapper<List<String>> {
        val url = "$baseUrl/products/categories"
        return makeWebRequest<List<String>, List<String>>(
            url = url,
            method = HttpMethod.Get,
        )
    }

    override suspend fun addToProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel> {
        val url = "$baseUrl/carts"
        return makeWebRequest(url = url,
            method = HttpMethod.Post,
            body = AddToCartRequest.fromCartRequestModel(request),
            mapper = { cartItem: CartResponse ->
                cartItem.toCartModel()
            })
    }

    override suspend fun login(email: String, password: String): ResultWrapper<UserModel> {
        val url = "$baseUrl/users"
        return makeWebRequest(url = url,
            method = HttpMethod.Post,
            body = LoginRequest(email, password),
            mapper = { user: UserResponse ->
                user.toDomainModel()
            })
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): ResultWrapper<UserModel> {
        val url = "$baseUrl/users"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Post,
            body = RegisterRequest(email,password,name), mapper = {user:UserResponse->user.toDomainModel()}
        )
    }


    suspend inline fun <reified T, R> makeWebRequest(
        url: String,
        method: HttpMethod,
        body: Any? = null,
        headers: Map<String, String> = emptyMap(),
        parameters: Map<String, String> = emptyMap(),
        noinline mapper: ((T) -> R)? = null
    ): ResultWrapper<R> {
        return try {
            val response: HttpResponse = client.request(url) {
                this.method = method
                url {
                    parameters.forEach { (key, value) ->
                        this.parameters.append(key, value)
                    }
                }
                headers.forEach { (key, value) ->
                    header(key, value)
                }
                if (body != null) {
                    setBody((body))
                }
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }

            val responseBody: String = response.body() // JSON string kimi alırıq
            val parsedResponse: T = Json.decodeFromString(responseBody) // JSON-u deserialize edirik

            val result: R = mapper?.invoke(parsedResponse) ?: parsedResponse as R
            ResultWrapper.Success(result)

        } catch (e: ClientRequestException) {
            ResultWrapper.Failure(e)
        } catch (e: TimeoutException) {
            ResultWrapper.Failure(e)
        } catch (e: ServerResponseException) {
            ResultWrapper.Failure(e)
        } catch (e: IOException) {
            Log.e("Api service error", "$e")
            ResultWrapper.Failure(e)
        } catch (e: Exception) {
            ResultWrapper.Failure(e)
        }
    }


}
