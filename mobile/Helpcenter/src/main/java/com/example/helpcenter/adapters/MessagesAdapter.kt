package com.example.helpcenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helpcenter.R
import com.example.helpcenter.databinding.*
import com.example.helpcenter.models.*
import com.example.helpcenter.utils.Constants.loggedInUser
import com.example.helpcenter.utils.DateUtilities
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KFunction1

class MessagesAdapter(
    private var messages: ArrayList<ChatItem>,
    private val addMessage: KFunction1<ChatItem.Message, Unit>,
    private val scrollToBottom: () -> Unit,
    private val getBotResponses: KFunction1<String, Unit>?

) :
    RecyclerView.Adapter<MessagesAdapter.BaseViewHolder<*>>() {

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 1
        private const val VIEW_TYPE_OTHER_MESSAGE = 2
        private const val VIEW_TYPE_OPTION = 3
        private const val VIEW_TYPE_DATE_MESSAGE = 4
        private const val VIEW_TYPE_TYPING_INDICATOR = 5
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(itemView: T)
    }

    inner class TypingIndicatorViewHolder(itemView: View) :
        BaseViewHolder<ChatItem.TypingIndicator>(itemView) {
        override fun bind(itemView: ChatItem.TypingIndicator) {}
    }

    inner class DateViewHolder(itemView: View) : BaseViewHolder<ChatItem.DateMessage>(itemView) {
        private val binding = LayoutDateFeedbackBinding.bind(itemView)
        override fun bind(itemView: ChatItem.DateMessage) {
            binding.tvDate.text = DateUtilities.getDateAsHumanReadableString(itemView.createdAt)
        }
    }

    inner class SelfMessageViewHolder(itemView: View) : BaseViewHolder<ChatItem.Message>(itemView) {
        private val binding = LayoutSelfMessageBinding.bind(itemView)

        override fun bind(itemView: ChatItem.Message) {
            binding.tvMessage.text = itemView.message
            binding.tvTime.text = DateUtilities.getTimeFromLocalDateTime(itemView.createdAt)
        }
    }

    inner class OtherMessageViewHolder(itemView: View) :
        BaseViewHolder<ChatItem.Message>(itemView) {
        private val binding = LayoutOtherMessageBinding.bind(itemView)

        override fun bind(itemView: ChatItem.Message) {
            binding.tvOtherMessage.text = itemView.message
            binding.tvOtherTime.text = DateUtilities.getTimeFromLocalDateTime(itemView.createdAt)

        }
    }

    inner class OptionsViewHolder(itemView: View) : BaseViewHolder<ChatItem.Option>(itemView) {
        private val binding = LayoutOptionBinding.bind(itemView)

        override fun bind(itemView: ChatItem.Option) {
            binding.tvOption.text = itemView.text

            binding.tvOption.setOnClickListener {

                for (value in messages.reversed()) {
                    if (value is ChatItem.Message) break
                    messages.remove(value)
                }

                addMessage(ChatItem.Message(itemView.text, Date(), from = loggedInUser.email))
                scrollToBottom()
                itemView.nextIntent?.let {
                    getBotResponses!!(it)
                }

            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = messages[position]) {
            is ChatItem.Message -> return when (item.from) {
                loggedInUser.email -> VIEW_TYPE_MY_MESSAGE
                else -> VIEW_TYPE_OTHER_MESSAGE
            }
            is ChatItem.TypingIndicator -> VIEW_TYPE_TYPING_INDICATOR
            is ChatItem.Option -> VIEW_TYPE_OPTION
            is ChatItem.DateMessage -> VIEW_TYPE_DATE_MESSAGE
            else -> VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            VIEW_TYPE_MY_MESSAGE ->
                SelfMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_self_message, parent, false)
                )
            VIEW_TYPE_OTHER_MESSAGE -> OtherMessageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_other_message, parent, false)
            )
            VIEW_TYPE_TYPING_INDICATOR -> TypingIndicatorViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_typing_indicator, parent, false)
            )
            VIEW_TYPE_OPTION ->
                OptionsViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_option, parent, false)
                )
            else -> DateViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_date_feedback, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = messages[position]

        when (holder) {
            is OptionsViewHolder -> holder.bind(element as ChatItem.Option)
            is SelfMessageViewHolder -> holder.bind(element as ChatItem.Message)
            is TypingIndicatorViewHolder -> holder.bind(element as ChatItem.TypingIndicator)
            is OtherMessageViewHolder -> holder.bind(element as ChatItem.Message)
            is DateViewHolder -> holder.bind(element as ChatItem.DateMessage)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}