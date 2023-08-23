package com.example.helpcenter.ui.chat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpcenter.R
import com.example.helpcenter.adapters.MessagesAdapter
import com.example.helpcenter.databinding.FragmentChatWithBotBinding
import com.example.helpcenter.models.ChatItem
import com.example.helpcenter.utils.Intents.CONTACT
import com.example.helpcenter.utils.Intents.START
import com.example.helpcenter.viewmodel.MessageViewModel
import com.example.helpcenter.viewmodel.ResponseViewModel
import kotlinx.coroutines.*
import java.util.*


class ChatWithBotFragment : Fragment() {

    companion object {
        private const val CHANNEL_ID: String = "New Message"
        private const val CHANNEL_NAME: String = "New Message"
    }

    private var _binding: FragmentChatWithBotBinding? = null
    private val binding get() = _binding!!
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val messages = arrayListOf<ChatItem>()
    private val messagesAdapter =
        MessagesAdapter(messages, ::addMessage, ::scrollToBottom, ::getNextResponse)

    private val viewModel: ResponseViewModel by activityViewModels()
    private val messageViewModel: MessageViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatWithBotBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.typing.visibility = View.GONE
        // Initialize the recycler view with a linear layout manager, adapter
        binding.messageList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.messageList.adapter = messagesAdapter

        initView()

        binding.backButton.setOnClickListener {
            requireActivity().finish()
        }
        binding.tvSupport.setOnClickListener {
            findNavController().navigate(R.id.action_ChatWithBotFragment_to_InboxFragment)
        }
    }

    private fun initView() {
        viewModel.resetResponse()
        viewModel.getResponse(START)
        if (messages.size <= 0)
            messages.add(0, ChatItem.DateMessage(Date()))
        createNotificationChannel()
        scrollToBottom()
        observeData()
    }

    private fun observeData() {
        viewModel.messages.observe(viewLifecycleOwner, {
            mainScope.launch {
                if (it != null) {
                    it.messages.forEach { message ->
                        showAndHideInterlocutorTyping()
                        addMessage(ChatItem.Message(message, Date()))
                        scrollToBottom()
                        // delay because we don't want the options to appear instantly.
                        delay(1000L)
                    }
                    it.options?.forEach { option -> messages.add(option) }
                    scrollToBottom()
                }
            }
        })
        viewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
        messageViewModel.lastMessage.observe(viewLifecycleOwner, {
            if (it != null) {
                val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                    .setContentTitle(it.from)
                    .setContentText(it.message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

                with(NotificationManagerCompat.from(requireContext())) {
                    notify(1, builder.build())
                }
            }
        })
    }

    /**
     * Shows a typing indicator to the user as feedback, because we don't want the chatbot
     * to respond immediately
     */
    private suspend fun showAndHideInterlocutorTyping() {
        // Show
        delay((200..600).random().toLong())
        val typingIndicator = ChatItem.TypingIndicator()
        messages.add(typingIndicator)
        messagesAdapter.notifyItemInserted(messages.size - 1)
        scrollToBottom()
        // Hide
        delay((600..1200).random().toLong())
        messages.remove(typingIndicator)
        messagesAdapter.notifyItemRemoved(messages.size - 1)
        scrollToBottom()
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
     * Adds the provided message to the view and in the background thread to the database
     * @param message is the message that needs to be added to the database and view
     */
    private fun addMessage(message: ChatItem.Message) {
        messages.add(message)
        messagesAdapter.notifyDataSetChanged()
    }

    /**
     * Gets the next intent of the chatbot with the help of the given previous intent
     * @param intent in type string that is required to get the next response of the chatbot
     */
    private fun getNextResponse(intent: String) {
        if (intent == CONTACT) findNavController().navigate(R.id.action_ChatWithBotFragment_to_InboxFragment)
        else viewModel.getResponse(intent)
    }

    /**
     * Creates the channel which is used by the notification manager
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val manager =
                activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(channel)

        }
    }
}