package com.example.floracareapp.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.floracareapp.MainActivity
import com.example.floracareapp.Flora_pages
import com.example.floracareapp.R
import com.example.floracareapp.api.api_resource
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.coroutines.launch

class navigation_home : Fragment() {
    private lateinit var firstColumn: LinearLayout
    private lateinit var secondColumn: LinearLayout

    companion object {
        fun newInstance() = navigation_home()
    }

    private lateinit var viewModel: NavigationHomeViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_navigation_home, container, false)
        firstColumn = view.findViewById(R.id.FirstColumn)
        secondColumn = view.findViewById(R.id.SecondColumn)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NavigationHomeViewModel::class.java)
        val all_data = api_resource()
        var pageCategory_name = arguments?.getString("flora_category")
        var iter = 0
        if (pageCategory_name.isNullOrEmpty() || pageCategory_name == "Все категории") {
            (activity as? MainActivity)?.act_bar("Все категории", "home")
            lifecycleScope.launch {
                try {
                    val pageList = all_data.getAllPages()
                    for (pageList in pageList) {
                        Log.d("PageItem", "Title: ${pageList.title}")
                        val title = pageList.title
                        val imageView = RoundedImageView(requireContext())
                        Glide.with(requireContext())
                            .load(pageList.image_url)
                            .into(imageView)
                        val pageBlock = createPageBlock(title, imageView, pageList.id, pageList.text, pageList.image_url)
                        if (iter % 2 == 0) {
                            firstColumn.addView(pageBlock)
                        } else {
                            secondColumn.addView(pageBlock)
                        }
                        iter++
                    }
                } catch (e: Exception) {
                    Log.e("PageError", "Error in navigation_home", e)
                }
            }
        } else {
            (activity as? MainActivity)?.act_bar(pageCategory_name.toString(), "home")
            lifecycleScope.launch {
                try {
                    val data = api_resource()
                    Log.d("Выбранная категория", pageCategory_name.toString())
                    val pageList = data.getCatPages(pageCategory_name.toString())
                    for (pageItem in pageList) {
                        Log.d("PageItem", "Title: ${pageItem.title}")
                        val title = pageItem.title
                        val text = pageItem.text
                        val imageView = RoundedImageView(requireContext())
                        Glide.with(requireContext())
                            .load(pageItem.image_url)
                            .into(imageView)
                        val pageBlock = createPageBlock(title, imageView, pageItem.id, text, pageItem.image_url)
                        if (iter % 2 == 0) {
                            firstColumn.addView(pageBlock)
                        } else {
                            secondColumn.addView(pageBlock)
                        }
                        iter++
                    }
                } catch (e: Exception) {
                    Log.e("PageError", "Error in navigation_home", e)
                }
            }
        }
    }

    private fun createPageBlock(title: String, image: RoundedImageView, id: Int, text: String, image_url: String): LinearLayout {
        val pageBlock = LinearLayout(requireContext())
        val padding_in_layout = resources.getDimensionPixelOffset(R.dimen.layout_padding)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            880/*LinearLayout.LayoutParams.WRAP_CONTENT*/
        )
        layoutParams.setMargins(padding_in_layout, padding_in_layout, padding_in_layout, padding_in_layout)
        pageBlock.layoutParams = layoutParams
        pageBlock.orientation = LinearLayout.VERTICAL
        val backgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background)
        pageBlock.background = backgroundDrawable

        val titleTextView = TextView(requireContext())
        titleTextView.text = title
        titleTextView.setTextAppearance(R.style.PageTitle)
        val paddingInPixels = resources.getDimensionPixelOffset(R.dimen.text_padding)
        titleTextView.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels)

        val imageSize = resources.getDimensionPixelOffset(R.dimen.image_size)
        val imageLayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            imageSize
        )
        val imageMargin = resources.getDimensionPixelOffset(R.dimen.image_margin)
        imageLayoutParams.setMargins(imageMargin, imageMargin, imageMargin, imageMargin)
        image.layoutParams = imageLayoutParams
        image.cornerRadius = resources.getDimensionPixelSize(R.dimen.image_corner_radius).toFloat()
        image.scaleType = ImageView.ScaleType.CENTER_CROP

        //val page_button = Button(requireContext())
        //page_button.text = "Читать далее"
        //val backgroundButton = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_button)
        //page_button.background = backgroundButton

        titleTextView.setOnClickListener {
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(300)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start()
                }
                .start()
            val intent = Intent(requireContext(), Flora_pages::class.java)
            intent.putExtra("page_title", title)
            intent.putExtra("text", text)
            intent.putExtra("image_url", image_url)
            intent.putExtra("id", id.toString())
            startActivity(intent)
        }
        pageBlock.addView(image)
        pageBlock.addView(titleTextView)
        //pageBlock.addView(page_button)
        return pageBlock
    }
}
