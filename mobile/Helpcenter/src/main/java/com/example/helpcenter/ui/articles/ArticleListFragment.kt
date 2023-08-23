package com.example.helpcenter.ui.articles

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpcenter.adapters.ArticleAdapter
import com.example.helpcenter.databinding.FragmentArticleListBinding
import com.example.helpcenter.models.ListItem
import com.example.helpcenter.R

import com.example.helpcenter.viewmodel.ArticleViewModel


class ArticleListFragment : Fragment() {

    private var _binding: FragmentArticleListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ArticleViewModel by activityViewModels()
    private val articlesAdapter = ArticleAdapter(::showDetails)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentArticleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeArticles()

        articlesAdapter.updateList(articlesAdapter.articles)

        val articleSearch = binding.etArticleSearchbar

        // When the user inputs text into the searchbar, it refreshes the search results with the new filter
        articleSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {

                // filter your list from your input
                filter(s.toString())
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        })
    }

    // This function takes all the existing articles and applies the filter with the search input to them, after which it returns a filtered list and updates the recyclerview
    fun filter(text: String?) {
        val temp: MutableList<ListItem> = ArrayList()

        if (text == "") {
            temp.addAll(articlesAdapter.articles)
        }
        else {
            for (theme in articlesAdapter.articles) {
                if (theme is ListItem.Theme) {
                    for (article in articlesAdapter.articles) {
                        if (article is ListItem.Article) {
                            if (article.title.contains(text?: "", ignoreCase = true)) {
                                if (article.theme == theme.title) {
                                    temp.add(theme)
                                    break
                                }
                            }
                        }
                    }
                    for (article in articlesAdapter.articles) {
                        if (article is ListItem.Article) {
                            if (article.title.contains(text ?: "", ignoreCase = true)) {
                                if (article.theme == theme.title) {
                                    temp.add(article)
                                }
                            }
                        }
                    }
                }
            }
        }
        //update recyclerview
        articlesAdapter.updateList(temp.toList())
    }

    private fun observeArticles() {
        binding.rvArticles.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL, false)
        binding.rvArticles.adapter = articlesAdapter

        viewModel.articles.observe(viewLifecycleOwner, {
          articlesAdapter.update(it)
        })
    }

    // If a article is pressed it will show the contents of the article
    private fun showDetails(article: ListItem.Article) {
        viewModel.fetchCurrentArticleContent(article)
        findNavController().navigate(
            R.id.action_ArticleListFragment_to_ArticleFragement
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}