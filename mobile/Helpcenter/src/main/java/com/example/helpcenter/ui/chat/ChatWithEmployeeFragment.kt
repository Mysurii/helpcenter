package com.example.helpcenter.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpcenter.adapters.MessagesAdapter
import com.example.helpcenter.databinding.FragmentChatWithEmployeeBinding
import com.example.helpcenter.models.Chat
import com.example.helpcenter.models.ChatItem
import com.example.helpcenter.repositories.SocketHandler
import com.example.helpcenter.utils.Constants.loggedInUser
import com.example.helpcenter.utils.DateUtilities
import com.example.helpcenter.viewmodel.MessageViewModel

class ChatWithEmployeeFragment : Fragment() {

    private val typingIndicator = ChatItem.TypingIndicator()
    private var _binding: FragmentChatWithEmployeeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MessageViewModel by activityViewModels()
    private val messages = arrayListOf<ChatItem>()
    private val messagesAdapter =
        MessagesAdapter(messages, ::logOption, ::scrollToBottom, null)
    private var activeChat: Chat? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatWithEmployeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SocketHandler.emitAddUser(null)
        initRv()
        scrollToBottom()

        if (activeChat != null) {
            viewModel.setTimeout {
                viewModel.fetchMessages(activeChat!!.id)
            }
        }
        observeData()
        listenForEvents()
    }

    private fun initRv() {
        binding.messageList.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = messagesAdapter
        }
    }

    private fun logOption(message: ChatItem.Message) {
        Log.d("option ->", message.toString())
    }

    private fun listenForEvents() {
        //Sets the active chat and changes the title to the email
        viewModel.activeChat.observe(viewLifecycleOwner, {
            this.activeChat = it
            binding.tvTitle.text = it.members.find { x -> x != loggedInUser.email }
        })
        // Handles the feedback to to user if an employee is online and/or typing
        viewModel.conversations.observe(viewLifecycleOwner, {
            if (activeChat != null) {
                val foundConversation = it.find { x -> x.id == activeChat!!.id }
                if (foundConversation != null) {
                    if (foundConversation.isOnline == true) {
                        binding.isOffline.visibility = View.GONE
                        binding.isOnline.visibility = View.VISIBLE
                    } else {
                        binding.isOffline.visibility = View.VISIBLE
                        binding.isOnline.visibility = View.GONE
                    }

                    if (foundConversation.isTyping == true)
                        showInterlocutorTyping()
                    else
                        hideInterlocutorTyping()
                }
            }
        })
        // Add message
        binding.fab.setOnClickListener {
            val text = binding.etMessage.text.toString()
            if (text.isNotEmpty()) {
                addMessage(text)
                binding.etMessage.setText("")
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        // Let's the employee know that the user is typing
        binding.etMessage.addTextChangedListener {
            if (activeChat != null) {
                val receiverEmail = activeChat!!.members.find { x -> x != loggedInUser.email }
                val conversationId = activeChat!!.id
                val isTyping = !it.isNullOrBlank()

                receiverEmail?.let { it1 ->
                    SocketHandler.emitIsTyping(
                        it1,
                        conversationId,
                        isTyping
                    )
                }
            }
        }
    }

    /**
     * Notifies the adapter that the messageslist is changed and scrolls to the bottom of
     * the view
     */
    private fun scrollToBottom() {
        if (messages.size > 0) {
            messagesAdapter.notifyItemChanged(messages.size - 1)
            binding.messageList.smoothScrollToPosition(messages.size - 1)
        }
    }


    /**
     * Adds a typing animation to the recyclerview
     */
    private fun showInterlocutorTyping() {
        messages.add(typingIndicator)
        messagesAdapter.notifyItemInserted(messages.size - 1)
        scrollToBottom()
    }

    /**
     * Removes the typing animation from the recyclerview
     */
    private fun hideInterlocutorTyping() {
        messages.remove(typingIndicator)
        messagesAdapter.notifyItemRemoved(messages.size - 1)
        scrollToBottom()
    }


    private fun observeData() {
        viewModel.messages.observe(viewLifecycleOwner, {
            messages.clear()

            binding.noMessages.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE

            it.forEachIndexed { index, message ->
                val prevIdx = if (index == 0) 0 else index - 1

                //Add start message
                if (prevIdx == 0) messages.add(ChatItem.DateMessage(message.createdAt))

                // Adds a new date message when the message is sent on another day
                if (!DateUtilities.isSameDay(message.createdAt, it[prevIdx].createdAt)) {
                    messages.add(ChatItem.DateMessage(message.createdAt))
                }
                messages.add(message)
            }
            messagesAdapter.notifyDataSetChanged()
            scrollToBottom()
        })
    }

    private fun addMessage(text: String) {
        if (activeChat != null) {
            viewModel.addMessage(text, activeChat!!.id)
            val receiverEmail = activeChat!!.members.find { x -> x != loggedInUser.email }
            if (receiverEmail != null) {
                SocketHandler.emitMessage(activeChat!!.id, loggedInUser.email, receiverEmail, text)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeActiveChat()
        val receiverEmail = activeChat?.members?.find { x -> x != loggedInUser.email }
        val conversationId = activeChat?.id
        if (receiverEmail != null && conversationId != null) {
            SocketHandler.emitIsTyping(receiverEmail, conversationId, false)
        }
        viewModel
        _binding = null
    }
}