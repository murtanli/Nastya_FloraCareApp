package com.example.floracareapp.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

data class PageResponse(
    val page: List<PageItem>
)
data class CategoryItem(
    val flora_category: String
)

data class CategoriesResponse(
    val categoryes: List<CategoryItem>
)


data class PageItem(
    val title: String,
    val text: String,
    val date: String,
    val image: String,
    val image_url: String,
    val category: Category,
    val id: Int
)

data class Category(
    val category: String
)

data class LoginResponse(
    val message: String,
    val user_data: UserData
)

data class UserData(
    val marked_Flora_page_id: List<Int>,
    val user_id: Int
)


class api_resource {

    suspend fun getAllPages(): List<PageItem> {
        val apiUrl = "http://testvar.ru:8100/get_all_pages/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                val pageResponse = gson.fromJson(response.toString(), PageResponse::class.java)
                pageResponse.page
            } catch (e: Exception) {
                Log.e("PagesError", "Error fetching or parsing page data", e)
                throw e
            }
        }
    }

    suspend fun getCatPages(category: String): List<PageItem> {
        val apiUrl = "http://testvar.ru:8100/get_page_cat/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с категорией
                val jsonInputString = "{\"flora_category\":\"$category\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                val pageResponse = gson.fromJson(response.toString(), PageResponse::class.java)
                pageResponse.page
            } catch (e: Exception) {
                Log.e("PageError", "Error fetching or parsing page data", e)
                throw e
            }
        }
    }

    suspend fun getAllCategories(): List<CategoryItem> {
        val apiUrl = "http://testvar.ru:8100/get_all_category/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                val categoriesResponse = gson.fromJson(response.toString(), CategoriesResponse::class.java)
                Log.e("Cat", categoriesResponse.toString())
                categoriesResponse.categoryes
            } catch (e: Exception) {
                Log.e("CategoryError", "Error fetching or parsing categories data", e)
                throw e
            }
        }
    }

    suspend fun logIn(login: String, password: String): LoginResponse {
        val apiUrl = "http://testvar.ru:8100/log_in/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"login\":\"$login\",\"password\":\"$password\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), LoginResponse::class.java)
            } catch (e: Exception) {
                Log.e("LoginError", "Error fetching or parsing login data ", e)
                throw e
            }
        }
    }

    suspend fun Sign_in(login: String, password: String): LoginResponse {
        val apiUrl = "http://testvar.ru:8100/sign_in/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"  // Используйте POST вместо GET
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-строку с логином и паролем
                val jsonInputString = "{\"login\":\"$login\",\"password\":\"$password\"}"

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), LoginResponse::class.java)
            } catch (e: Exception) {
                Log.e("LoginError", "Error fetching or parsing login data ", e)
                throw e
            }
        }
    }

    suspend fun SaveMarkedPage(pageIds: JsonArray, userId: String): LoginResponse {
        val apiUrl = "http://testvar.ru:8100/save_marked_page/"
        val url = URL(apiUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Создаем JSON-объект с идентификаторами новостей и пользователя
                val jsonObject = JsonObject().apply {
                    addProperty("user_id", userId)
                    add("marked_Flora_page_id", pageIds)
                }
                val jsonInputString = jsonObject.toString()

                // Отправляем JSON в тело запроса
                val outputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.close()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val gson = Gson()
                gson.fromJson(response.toString(), LoginResponse::class.java)
            } catch (e: Exception) {
                Log.e("SavePageError", "Error fetching or parsing save page data", e)
                throw e
            }
        }
    }

}