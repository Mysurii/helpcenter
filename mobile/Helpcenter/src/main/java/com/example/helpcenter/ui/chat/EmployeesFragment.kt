package com.example.helpcenter.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpcenter.R
import com.example.helpcenter.adapters.NewUserAdapter
import com.example.helpcenter.databinding.FragmentEmployeesBinding
import com.example.helpcenter.viewmodel.MessageViewModel

class EmployeesFragment : DialogFragment() {
    private var _binding: FragmentEmployeesBinding? = null
    private val binding get() = _binding!!

    private var users = arrayListOf<String>()
    private val userAdapter = NewUserAdapter(users, ::startConversation)
    private val viewModel: MessageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEmployeesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWidthToMatchParent()
        initRv()

        viewModel.employees.observe(viewLifecycleOwner, {
            users.clear()
            users.addAll(it)
            userAdapter.notifyDataSetChanged()
        })

        binding.btnBack.setOnClickListener {
            dismiss()
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

    /**
     * Increases the size of the dialog
     */
    private fun DialogFragment.setupWidthToMatchParent() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * Starts aa conversation with the employee
     */
    private fun startConversation(email: String) {
        viewModel.createConversation(email)
        dismiss()
        findNavController().navigate(R.id.action_InboxFragment_to_ChatWithEmployeeFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}