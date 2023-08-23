package com.example.helpcenter.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpcenter.R
import com.example.helpcenter.adapters.InboxUserAdapter
import com.example.helpcenter.databinding.FragmentInboxBinding
import com.example.helpcenter.models.Chat
import com.example.helpcenter.repositories.SocketHandler
import com.example.helpcenter.viewmodel.MessageViewModel

class InboxFragment : Fragment() {
    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!
    private var conversations = arrayListOf<Chat>()
    private val userAdapter = InboxUserAdapter(conversations, ::redirectToChat)
    private val viewModel: MessageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInboxBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
        viewModel.setTimeout { viewModel.fetchEmployees() }

        observeConversations()

        binding.ibBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.btnAdd.setOnClickListener {
            val dialog = EmployeesFragment()
            dialog.show(parentFragmentManager, "customDialog")
        }
    }

    private fun initRv() {
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = userAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireActivity(), DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun observeConversations() {
        viewModel.conversations.observe(viewLifecycleOwner, {
            this.conversations.clear()
            this.conversations.addAll(it)
            userAdapter.notifyDataSetChanged()
            if (conversations.isNotEmpty()) binding.tvNoMessages.visibility = View.GONE
        })
        viewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun redirectToChat(id: String) {
        viewModel.setActiveChat(id)
        SocketHandler.emitAddUser(id)
        viewModel.setTimeout { viewModel.fetchMessages(id) }
        findNavController().navigate(R.id.action_InboxFragment_to_ChatWithEmployeeFragment)
    }
}