package com.example.helpcenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helpcenter.R
import com.example.helpcenter.databinding.LayoutArticleItemBinding
import com.example.helpcenter.databinding.LayoutArticleThemeBinding
import com.example.helpcenter.models.*
import kotlin.reflect.KFunction1

class ArticleAdapter(
    private val showDetails: KFunction1<ListItem.Article, Unit>
) :
    RecyclerView.Adapter<ArticleAdapter.BaseViewHolder<*>>() {

    val articles = mutableListOf<ListItem>()
    var displayedItems = listOf<ListItem>()

    companion object {
        private const val VIEW_TYPE_ARTICLE = 1
        private const val VIEW_TYPE_THEME = 2

    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(itemView: T)
    }


    inner class ArticleViewHolder(itemView: View) : BaseViewHolder<ListItem.Article>(itemView) {
        val binding = LayoutArticleItemBinding.bind(itemView)

        override fun bind(itemView: ListItem.Article) {
            binding.tvArticleItem.text = itemView.title
            binding.constraintLayoutArticleItem.setOnClickListener {
                showDetails(itemView)

            }
        }
    }

    inner class ThemeViewHolder(itemView: View) : BaseViewHolder<ListItem.Theme>(itemView) {
        val binding = LayoutArticleThemeBinding.bind(itemView)

        override fun bind(itemView: ListItem.Theme) {
            binding.tvArticleTheme.text = itemView.title
            if (displayedItems.indexOf(itemView) == 0) {
                //binding.divider2.visibility = View.GONE
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (displayedItems[position]) {
            is ListItem.Article -> VIEW_TYPE_ARTICLE
            is ListItem.Theme -> VIEW_TYPE_THEME

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            VIEW_TYPE_ARTICLE -> ArticleViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_article_item, parent, false)
            )
            else -> ThemeViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_article_theme, parent, false)
            )
        }
    }

    override fun getItemCount() = displayedItems.size


    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item = displayedItems[position]

        when (holder) {
            is ArticleViewHolder -> holder.bind(item as ListItem.Article)
            is ThemeViewHolder -> holder.bind(item as ListItem.Theme)
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    fun updateList(list: List<ListItem>) {
        displayedItems = list
        notifyDataSetChanged()
    }

    fun update(list: List<ListItem.Article>) {
        articles.clear()
        val groupedItems = list.groupBy { it.theme }
        articles.addAll(groupedItems.flatMap { (key, value) -> listOf(ListItem.Theme(key)) + value.map { it } })
        notifyDataSetChanged()
    }
}