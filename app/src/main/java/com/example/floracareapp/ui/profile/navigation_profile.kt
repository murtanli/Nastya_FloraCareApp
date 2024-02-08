package com.example.floracareapp.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.floracareapp.MainActivity
import com.example.floracareapp.Flora_pages
import com.example.floracareapp.R
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.coroutines.launch
import com.example.floracareapp.api.api_resource
import com.example.floracareapp.info_into_phone.data
import com.example.floracareapp.ui.profile.AuthActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay

class navigation_profile : Fragment() {

    companion object {
        fun newInstance() = navigation_profile()
    }

    private lateinit var viewModel: NavigationProfileViewModel
    private lateinit var Profile_login: TextView
    private lateinit var textView5: TextView
    private lateinit var button_exit: Button
    private lateinit var saved_pageContainer: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_navigation_profile, container, false)
        Profile_login = view.findViewById(R.id.textView4)
        button_exit = view.findViewById(R.id.button_exit)
        textView5 = view.findViewById(R.id.textView5)
        saved_pageContainer = view.findViewById(R.id.saved_pageContainer)

        return view
    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NavigationProfileViewModel::class.java)


        //(activity as? MainActivity)?.act_bar("Профиль", "profile")

        val sharedPreferences = context?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val login = sharedPreferences?.getString("login", null)
        val id_profile = sharedPreferences?.getString("id_profile", null)
        val password = sharedPreferences?.getString("password", null)
        Log.d("IdProfile", id_profile.toString()) // вывод айди пользователя

        if (!login.isNullOrBlank()) {
            (activity as? MainActivity)?.act_bar("Профиль ${login.toString()}", "profile")
            Profile_login.text = login.toString()
        } else {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            //если не авторизован то выходит из приложения
            startActivity(intent)
            requireActivity().finish()
        }

        //Выход с аккаунта
        button_exit.setOnClickListener {
            val sharedPreferences = context?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()

            editor?.remove("login")
            editor?.remove("id_profile")
            editor?.remove("password")

            editor?.apply()

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        // Объявляем переменную idsList до блока try-catch
        var idsList: List<Int>? = null
        try {
            val savedData = sharedPreferences?.getString("id", "")
            val idArray = savedData?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()

            idsList = idArray



        } catch (e: Exception) {
            // Обработка исключения, если необходимо
            e.printStackTrace()
        }

        //показ сохраненных новостей
        val all_data = api_resource()
        lifecycleScope.launch {
            try {
                val pageList = all_data.getAllPages()
                for (pageItem in pageList) {
                    Log.d("PageItem", "Title: ${pageItem.title}")
                    val title = pageItem.title

                    // Используйте Glide для загрузки изображения по URL
                    val imageView = RoundedImageView(requireContext())
                    Glide.with(requireContext())
                        .load(pageItem.image_url)
                        .into(imageView)
                    if (idsList != null) {
                        if (pageItem.id in idsList) {
                            val pageBlock = createPageBlock(title, imageView, pageItem.id, pageItem.text,pageItem.image_url)
                            saved_pageContainer.addView(pageBlock)
                        }
                    }

                }
                // Обработка результата
            } catch (e: Exception) {
                Log.e("PageError", "Error in navigation_home", e)
                // Обработка ошибки
            }
        }

        var i = 0
        Profile_login.setOnClickListener {
            i += 1
            Toast.makeText(context, "Осталось нажать ${3 - i}", Toast.LENGTH_SHORT).show()

            if (i == 3) {
                val yourText = password

                // Получение менеджера буфера обмена
                val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?

                // Создание объекта ClipData для хранения текста
                val clipData = ClipData.newPlainText("label", yourText)

                // Установка данных в буфер обмена
                clipboardManager?.setPrimaryClip(clipData)

                Toast.makeText(context, "Пароль скопирован в буфер обмена", Toast.LENGTH_SHORT).show()
                i = 0
            }
        }

    }

    private fun createPageBlock(title: String, image: RoundedImageView, id: Int, text: String, image_url: String): LinearLayout {
        val PageBlock = LinearLayout(requireContext())
        val padding_in_layout= resources.getDimensionPixelOffset(R.dimen.layout_padding)


        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)
        PageBlock.layoutParams = layoutParams

        PageBlock.orientation = LinearLayout.HORIZONTAL

        // Стилизация LinearLayout в котором присоеденил блок rounded_background с белым цветом и закругленными углами
        val backgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background)
        PageBlock.background = backgroundDrawable

        // Стилизация текста
        val titleTextView = TextView(requireContext())
        titleTextView.text = title
        titleTextView.setTextAppearance(R.style.PageTitle)
        // Установка отступа для текста внутри блока новости
        val paddingInPixels = resources.getDimensionPixelOffset(R.dimen.text_padding)
        titleTextView.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels)

        // Стилизация изображения
        // Получение размеров изображения в ресурсах
        val imageSize = resources.getDimensionPixelOffset(R.dimen.image_size)
        val imageLayoutParams = LinearLayout.LayoutParams(
            200, // Ширина изображения будет равна ширине родительского элемента
            200
        )
        val imageMargin = resources.getDimensionPixelOffset(R.dimen.image_margin)
        imageLayoutParams.setMargins(imageMargin, imageMargin, imageMargin, imageMargin)

        image.layoutParams = imageLayoutParams

        image.cornerRadius = resources.getDimensionPixelSize(R.dimen.image_corner_radius).toFloat()
        image.scaleType = ImageView.ScaleType.CENTER_CROP

        titleTextView.setOnClickListener {
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(300)
                .withEndAction {
                    // Возвращение к обычному размеру после анимации
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start()
                }
                .start()
            val intent = Intent(requireContext(),Flora_pages ::class.java)
            intent.putExtra("page_title", title)
            intent.putExtra("text", text)
            intent.putExtra("image_url", image_url)
            intent.putExtra("id", id.toString())

            startActivity(intent)
        }
        PageBlock.addView(image)
        PageBlock.addView(titleTextView)
        //PageBlock.addView(page_button)



        return PageBlock
    }

}