package com.example.helpcenter.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.helpcenter.R
import com.example.helpcenter.databinding.FragmentHelpcenterBinding
import com.example.helpcenter.ui.articles.ArticlesActivity
import com.example.helpcenter.ui.chat.ChatActivity
import com.example.helpcenter.utils.Constants

class HelpcenterFragment : Fragment() {

    private var _binding: FragmentHelpcenterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpcenterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Constants.loggedInUser.isAdmin) {
            binding.chatMetEenExpertBtn.text = getString(R.string.inbox)
        }

        // The FAQ article list button
        binding.probleemZelfOplossenBtn.setOnClickListener {
            val intent = Intent(context, ArticlesActivity::class.java)
            startActivity(intent)
        }
        // The chatbot button
        binding.chatMetEenExpertBtn.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("isInboxFragment", false)
            startActivity(intent)
        }
        // The livechat inbox button
        binding.chatInboxBtn.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("isInboxFragment", true)
            startActivity(intent)
        }
        // The contact form button
        binding.contactformButton.setOnClickListener {
            val intent = Intent(context, ContactFormActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}