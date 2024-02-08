package com.example.floracareapp.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.floracareapp.MainActivity
import com.example.floracareapp.R
import com.example.floracareapp.ui.home.navigation_home
import com.example.floracareapp.api.api_resource
import kotlinx.coroutines.launch

class navigation_category : Fragment() {

    private lateinit var catContainer: LinearLayout
    companion object {
        fun newInstance() = navigation_category()
    }

    private lateinit var viewModel: NavigationCategoryViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_navigation_category, container, false)
        catContainer = view.findViewById(R.id.CatTable)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NavigationCategoryViewModel::class.java)

        if (isAdded) {
            (activity as? MainActivity)?.act_bar("Категории", "category")
        }

        lifecycleScope.launch {
            try {
                val data = api_resource()
                val categoriesList = data.getAllCategories()
                for (categoryItem in categoriesList) {
                    Log.d("CategoryItem", "Category: ${categoryItem.flora_category}")
                    val catblock = createCategoryBlock(categoryItem.flora_category)
                    catContainer.addView(catblock)
                }
            } catch (e: Exception) {
                Log.e("CategoryError", "Error in navigation_category", e)
                // Обработка ошибки
            }
            val catblock = createCategoryBlock("Все категории")
            catContainer.addView(catblock)
        }
    }


    private fun createCategoryBlock(title: String): LinearLayout {
        val catBlock = LinearLayout(requireContext())

        // Установка отступов для LinearLayout (10 пикселей со всех сторон)
        val paddingInPixels = resources.getDimensionPixelOffset(R.dimen.text_padding)
        catBlock.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels)

        // Установка параметров макета для LinearLayout (ширина - wrap_content, высота - wrap_content, гравитация - по центру)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            250/*LinearLayout.LayoutParams.WRAP_CONTENT*/
        )
        layoutParams.gravity = Gravity.CENTER
        catBlock.layoutParams = layoutParams

        val buttonblock = Button(requireContext())
        buttonblock.text = title
        buttonblock.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))


        // Установка фона для кнопки
        val backgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.cat_button)
        buttonblock.background = backgroundDrawable

        // Установка фиксированного размера для кнопки (ширина - 100 пикселей, высота - 50 пикселей)
        val buttonLayoutParams = LinearLayout.LayoutParams(
            resources.getDimensionPixelOffset(R.dimen.but_width),
            resources.getDimensionPixelOffset(R.dimen.but_height)
        )
        buttonblock.layoutParams = buttonLayoutParams

        // Добавление кнопки в LinearLayout
        catBlock.addView(buttonblock)

        // Добавление анимации при нажатии на кнопку
        buttonblock.setOnClickListener {
            it.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(150)
                .withEndAction {
                    // Возвращение к обычному размеру после анимации
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start()
                }
                .start()

            val bundle = Bundle()
            bundle.putString("flora_category", title)
            val fragmentHome = navigation_home()
            fragmentHome.setArguments(bundle)
            findNavController().navigate(R.id.navigation_home, bundle)
        }
        return catBlock
    }








}