package com.example.helpcenter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpcenter.R
import com.example.helpcenter.databinding.LayoutArticleContentHeaderBinding
import com.example.helpcenter.databinding.LayoutArticleContentItemBinding
import com.example.helpcenter.models.Content
import com.example.helpcenter.models.Header


class ContentAdapter(
    private val contents: ArrayList<Any>
) :
    RecyclerView.Adapter<ContentAdapter.BaseViewHolder<*>>() {
    private lateinit var context: Context

    companion object {
        private const val VIEW_TYPE_CONTENT = 1
        private const val VIEW_TYPE_HEADER = 2
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(itemView: T)
    }


    inner class ContentViewHolder(itemView: View) : BaseViewHolder<Content>(itemView) {
        val binding = LayoutArticleContentItemBinding.bind(itemView)

        override fun bind(itemView: Content) {
            binding.tvHeaderInsideArticle.text = itemView.headline
            binding.tvTextInsideArticle.text = itemView.description
            binding.btnContactButton.visibility = View.GONE
            binding.vvVideoInsideArticle.visibility = View.GONE

            if (itemView.url != null && itemView.url!!.isNotEmpty()) {
//                val webView = binding.vvVideoInsideArticle
//                webView.settings.javaScriptEnabled = true
//                webView.webChromeClient = WebChromeClient()
//
//                webView.loadData(getHTML(itemView.url!!), "text/html", "UTF-8")

                Glide.with(context).load(itemView.url).into(binding.ivImageInsideArticle)

            } else {

                binding.ivImageInsideArticle.visibility = View.GONE
            }
        }
    }

    inner class HeaderViewHolder(itemView: View) : BaseViewHolder<Header>(itemView) {
        val binding = LayoutArticleContentHeaderBinding.bind(itemView)

        override fun bind(itemView: Header) {
            binding.tvHeadline.text = itemView.title
            binding.tvDescription.text = itemView.description
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (contents[position]) {
            is Content -> VIEW_TYPE_CONTENT
            is Header -> VIEW_TYPE_HEADER
            else -> throw IllegalArgumentException("Invalid model Type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        this.context = parent.context

        return when (viewType) {
            VIEW_TYPE_CONTENT -> ContentViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_article_content_item, parent, false)
            )
            else -> HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_article_content_header, parent, false)
            )
        }
    }

    override fun getItemCount() = contents.size


    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item = contents[position]

        when (holder) {
            is ContentViewHolder -> holder.bind(item as Content)
            is HeaderViewHolder -> holder.bind(item as Header)
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    fun getHTML(id: String): String {
        return """<iframe class="youtube-player" style="border: 0; width: 100%; height: 96%;padding:0px; margin:0px" id="ytplayer" type="text/html" src="https://www.youtube.com/embed/$id" frameborder="0" allowfullscreen autobuffer controls onclick="this.play()">
</iframe>
"""
    }

}