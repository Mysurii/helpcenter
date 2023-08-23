package com.example.helpcenter.ui.articles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpcenter.adapters.ContentAdapter
import com.example.helpcenter.databinding.FragmentArticleBinding
import com.example.helpcenter.models.Header
import com.example.helpcenter.viewmodel.ArticleViewModel

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private val contents = arrayListOf<Any>()
    private val contentAdapter = ContentAdapter(contents)

    private val viewModel: ArticleViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvContent.adapter = contentAdapter
        binding.rvContent.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        observeArticleContent()
    }


    private fun observeArticleContent() {
        viewModel.currentArticle.observe(viewLifecycleOwner, {
            contents.clear()
            contents.add(Header(it.title, it.description))
            contents.addAll(it.contents)
            contentAdapter.notifyDataSetChanged()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeCurrentArticle()
        _binding = null
    }

}