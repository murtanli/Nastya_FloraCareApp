package com.example.floracareapp.info_into_phone

import android.content.Context
import android.util.Log
import com.example.floracareapp.api.api_resource
import com.google.gson.JsonArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class data(private val context: Context) {

    fun save_phone(value: Int) {
        try {
            val sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

            // Получаем текущий набор значений из SharedPreferences
            val idSet = sharedPreferences.getString("id", "") ?: ""

            // Создаем новый набор значений и добавляем в него новое значение
            val newIdSet = "$idSet,$value"

            // Сохраняем обновленный набор значений в SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("id", newIdSet)
            editor.apply()

            val savedData = sharedPreferences.getString("id", "")
            val idArray = savedData?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()
            //val idArray = intArrayOf(1, 2, 3, 4)
            val jsonArray = JsonArray()
            idArray.forEach { jsonArray.add(it) }

            val id_profile = sharedPreferences?.getString("id_profile", null)
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val data = api_resource()
                    val result = data.SaveMarkedPage(jsonArray, id_profile.toString())
                    Log.d("ACC", id_profile.toString())
                    if (result != null) {
                        // Обработка успешного результата
                        Log.d("Succsesful", result.message)
                    } else {
                        // Обработка случая, когда result равен null
                        Log.e("SavePagesActivity", "Save pages failed - result is null")
                        Log.d("result - null", result.message)
                    }
                } catch (e: Exception) {
                    // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                    Log.e("SavePagesActivity", "Error during saving pages", e)
                    e.printStackTrace()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ERROR_SAVE", "ОШИБКА")
        }
    }



    fun get_saved_data_phone(): List<Int> {
        val sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

        // Объявляем переменную idsList до блока try-catch
        var idsList: List<Int>? = null

        try {
            val savedData = sharedPreferences.getString("id", "")
            val idArray = savedData?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()

            idsList = idArray

            for (i in idsList) {
                Log.d("SavedData", "ID: $i")
            }

        } catch (e: Exception) {
            // Обработка исключения, если необходимо
            e.printStackTrace()
        }

        // Возвращаем список целых чисел (List<Int>), может быть null
        return idsList ?: emptyList()
    }


    fun delete_saved_data_phone(id: Int) {
        val sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

        // Получаем текущую строку данных
        val savedData = sharedPreferences.getString("id", "")

        // Преобразовываем строку в список, удаляем элемент и обновляем строку
        val idList = savedData?.split(",")?.toMutableList() ?: mutableListOf()
        idList.remove(id.toString())

        // Обновляем строку с использованием разделителя ","
        val updatedData = idList.joinToString(",")

        // Сохраняем обновленную строку данных
        val editor = sharedPreferences.edit()
        editor.putString("id", updatedData)
        editor.apply()

        val sharedPreferences2 = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val savedData2 = sharedPreferences2.getString("id", "")
        val idArray = savedData2?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()
        val id_profile = sharedPreferences2?.getString("id_profile", null)
        Log.e("Array", idArray.toString())
        val jsonArray = JsonArray()
        idArray.forEach { jsonArray.add(it) }
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = api_resource()
                val result = data.SaveMarkedPage(jsonArray, id_profile.toString())
                Log.d("ACC", id_profile.toString())
                if (result != null) {
                    // Обработка успешного результата
                    Log.d("Succsesful", result.message)
                } else {
                    // Обработка случая, когда result равен null
                    Log.e("SavePageActivity", "Save page failed - result is null")
                    Log.d("result - null", result.message)
                }
            } catch (e: Exception) {
                // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                Log.e("SavePageActivity", "Error during saving page", e)
                e.printStackTrace()
            }
        }
    }

}
