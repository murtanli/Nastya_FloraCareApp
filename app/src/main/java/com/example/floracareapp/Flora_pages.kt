package com.example.floracareapp


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.floracareapp.R
import com.example.floracareapp.info_into_phone.data
import com.example.floracareapp.api.api_resource

class Flora_pages : AppCompatActivity() {
    private lateinit var text1 : TextView
    private lateinit var text2 : TextView
    private lateinit var text3 : TextView
    private lateinit var image : ImageView
    @SuppressLint("MissingInflatedId", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flora_pages)

        //Убрал верхнюю панель
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false)
        }

        text1 = findViewById(R.id.pageTitle)
        text2 = findViewById(R.id.pageContent)
        text3 = findViewById(R.id.textSave)
        image = findViewById(R.id.pageImage)

        text1.setTextColor(resources.getColor(R.color.black))
        text1.setTextSize(resources.getDimensionPixelSize(R.dimen.text_title).toFloat())

        text2.setTextSize(resources.getDimensionPixelSize(R.dimen.text_content).toFloat())
        // Здесь вы можете получить данные о новости из Intent
        val pageTitle = intent.getStringExtra("page_title")
        val text = intent.getStringExtra("text")
        val image_url = intent.getStringExtra("image_url")
        val id_page = intent.getStringExtra("id")
        Log.e("Image", image_url.toString())
        Glide.with(this)
            .load(image_url)
            .into(image)

        // Затем используйте данные для отобр   ажения полной новости в макете
        text1.text = pageTitle
        text2.text = text
        //image.setImageResource(R.drawable.rab)
        // Добавление кнопки "Назад" в ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val all_id_dat = data(this)
        val pag_id_all = all_id_dat.get_saved_data_phone()
        if (id_page != null) {
            if (pag_id_all.contains(id_page.toInt())) {
                text3.text = "Сохранено !"
                text3.setTextColor(ContextCompat.getColor(this, R.color.zelen))
            }
        }
        text3.setOnClickListener {
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(300)
                .withEndAction {
                    if (text3.text == "Сохранить") {
                        //Toast.makeText(this, image_but.contentDescription, Toast.LENGTH_SHORT).show()
                        //image_but.contentDescription = "delete"
                        //image_but.setImageResource(R.drawable.baseline_delete_forever_24)
                        text3.text = "Сохранено !"
                        text3.setTextColor(ContextCompat.getColor(this, R.color.zelen))


                        val add_dat = data(this)
                        if (id_page != null) {
                            add_dat.save_phone(id_page.toInt())
                        }

                    } else if (text3.text == "Сохранено !") {
                        text3.text = "Сохранить"
                        text3.setTextColor(ContextCompat.getColor(this, R.color.black))
                        val add_dat = data(this)
                        if (id_page != null) {
                            add_dat.delete_saved_data_phone(id_page.toInt())
                        }
                    }
                    // Возвращение к обычному размеру после анимации
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start()
                }
                .start()
        }


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Эмулируем нажатие системной кнопки "Назад"
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}