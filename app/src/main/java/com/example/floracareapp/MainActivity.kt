package com.example.floracareapp



import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.floracareapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat.getColor

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_category, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Устанавливаем цвета текста для элементов меню
        setMenuItemColors(navView)
    }

    private fun setMenuItemColors(bottomNavigationView: BottomNavigationView) {
        val normalColor = getColor(this, R.color.zelen)
        val selectedColor = getColor(this, R.color.black)

        val menu = bottomNavigationView.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val itemTitle = menuItem.title.toString()

            // Устанавливаем цвет текста
            val normalSpannable = SpannableString(itemTitle)
            normalSpannable.setSpan(ForegroundColorSpan(normalColor), 0, itemTitle.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            menuItem.title = normalSpannable

            val selectedSpannable = SpannableString(itemTitle)
            selectedSpannable.setSpan(ForegroundColorSpan(selectedColor), 0, itemTitle.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            menuItem.setTitle(selectedSpannable)

        }
    }




    fun act_bar(text: String, nav_name: String) {
        // Получаем ActionBar
        val actionBar: ActionBar? = supportActionBar

        if (actionBar != null && nav_name == "home") {
            // Показываем ActionBar
            actionBar.show()

            // Устанавливаем пользовательский макет для ActionBar
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
            actionBar.setCustomView(R.layout.custom_action_bar)

            // Находим TextView в пользовательском макете и устанавливаем текст
            val titleTextView: TextView? = actionBar.customView.findViewById(R.id.action_bar_title)
            titleTextView?.text = text
        } else {
            // Скрываем ActionBar
            actionBar?.hide()
        }
    }

}