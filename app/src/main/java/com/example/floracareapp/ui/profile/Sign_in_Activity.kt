package com.example.floracareapp.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.example.floracareapp.MainActivity
import com.example.floracareapp.R
import com.example.floracareapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Sign_in_Activity : AppCompatActivity() {

    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextPassword2: EditText
    private lateinit var textView6: TextView
    private lateinit var buttonLogin: Button

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        editTextLogin = findViewById(R.id.editTextLogin)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextPassword2 = findViewById(R.id.editTextPassword2)
        textView6 = findViewById(R.id.textView6)
        buttonLogin = findViewById(R.id.buttonLogin)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.show()
            actionBar.title = "Регистрация"

            // Добавить кнопку "Назад"
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        buttonLogin.setOnClickListener {

            if (!editTextLogin.text.isNullOrEmpty() && !editTextPassword.text.isNullOrEmpty() && !editTextPassword2.text.isNullOrEmpty() && editTextPassword.text in editTextPassword2.text) {
                val loginText = editTextLogin?.text?.toString()
                val passwordText = editTextPassword?.text?.toString()
                GlobalScope.launch(Dispatchers.Main) {
                    try {

                        val data = api_resource()
                        val result = data.Sign_in(loginText.toString(), passwordText.toString())

                        if (result != null) {
                            val intent = Intent(this@Sign_in_Activity, AuthActivity::class.java)
                            startActivity(intent)
                            textView6.text = result.message

                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            textView6.text = "Ошибка в процессе авторизации $result.message"
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        textView6.text = "Ошибка входа: Неправильный пароль или профиль уже существует"
                    }
                }
            } else {
                textView6.text = "Пустые поля ! либо пороли не совпадают"
            }
        }

    }

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