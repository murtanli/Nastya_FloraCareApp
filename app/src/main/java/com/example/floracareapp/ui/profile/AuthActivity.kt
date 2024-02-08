package com.example.floracareapp.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.lifecycleScope
import com.example.floracareapp.MainActivity
import com.example.floracareapp.R
import com.example.floracareapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.content.SharedPreferences
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavInflater
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.floracareapp.ui.home.navigation_home
import com.example.floracareapp.ui.profile.Sign_in_Activity
import kotlinx.coroutines.withContext

class AuthActivity : AppCompatActivity() {

    private lateinit var but_reg : TextView
    private lateinit var buttonLogin : TextView
    private lateinit var ErrorText : TextView
    private lateinit var login : EditText
    private lateinit var password : EditText


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        but_reg = findViewById(R.id.textView2)
        buttonLogin = findViewById(R.id.buttonLogin)
        ErrorText = findViewById(R.id.textView3)
        login = findViewById(R.id.editTextLogin)
        password = findViewById(R.id.editTextPassword)

        // Убрать верхнюю панель
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.show()
            actionBar.title = "Авторизация"

            // Добавить кнопку "Назад"
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        ErrorText.text = ""
        val color = ContextCompat.getColor(this, R.color.red)
        ErrorText.setTextColor(color)

        buttonLogin.setOnClickListener {
            val loginText = login?.text?.toString()
            val passwordText = password?.text?.toString()

            if (loginText.isNullOrBlank() || passwordText.isNullOrBlank()) {
                ErrorText.text = "Не введены данные в поля"
            } else {
                ErrorText.text = ""
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        // Вызываем функцию logIn для выполнения запроса
                        val data = api_resource()
                        val result = data.logIn(login.text.toString(), password.text.toString())

                        if (result != null) {
                            if (result.message == "Авторизация успешна") {
                                // Если успешно авторизованы, выводим сообщение об успешной авторизации и обрабатываем данные
                                Log.d("LoginActivity", "Login successful")
                                Log.d("LoginActivity", "User ID: ${result.user_data.user_id}")

                                val shPrprofile = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                                val editor_pr = shPrprofile?.edit()

                                val login_pr = login.text
                                val password_pr = password.text
                                val user_id = result.user_data.user_id

                                editor_pr?.putString("login", login_pr.toString())
                                editor_pr?.putString("password", password_pr.toString())
                                editor_pr?.putString("id_profile", user_id.toString())
                                editor_pr?.apply()

                                val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()

                                // Очищаем предыдущие данные
                                editor.remove("id")

                                // Добавляем новые данные в виде строки с разделителями
                                editor.putString("id", result.user_data.marked_Flora_page_id.joinToString(","))

                                editor.apply()

                                // Чтение данных из SharedPreferences
                                val savedData = sharedPreferences.getString("id", "")
                                val idList = savedData?.split(",")?.toList() ?: emptyList()

                                for (i in idList) {
                                    Log.e("eeee", i)
                                }

                                for (i in result.user_data.marked_Flora_page_id) {
                                    Log.d("LoginActivity", "Marked Pages: $i")
                                }

                                ErrorText.text = result.message

                                ErrorText.setTextColor(R.color.blue)
                                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                // Если произошла ошибка, выводим сообщение об ошибке
                                Log.e("LoginActivity", "Login failed")
                                ErrorText.text = result.message
                            }
                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            ErrorText.text = "Ошибка в процессе авторизации $result.message"
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        ErrorText.text = "Ошибка входа: Неправильный пароль или профиль не найден"
                    }
                }

            }
        }

        but_reg.setOnClickListener {
            val intent = Intent(this, Sign_in_Activity::class.java)
            startActivity(intent)
        }
    }


    // Обработчик нажатия на кнопку "Назад"
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Здесь укажите активность, в которую вы хотите перейти
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Закрыть текущую активность
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

