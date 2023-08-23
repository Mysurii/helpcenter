package com.example.helpcenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helpcenter.R
import com.example.helpcenter.databinding.ItemInboxUserBinding
import com.example.helpcenter.models.Chat
import com.example.helpcenter.utils.Constants.loggedInUser
import com.example.helpcenter.utils.DateUtilities.getUserCardDate

class InboxUserAdapter(
    private val chats: List<Chat>,
    private val redirectToChat: (String) -> Unit
) :
    RecyclerView.Adapter<InboxUserAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemInboxUserBinding.bind(itemView)

        fun databind(chat: Chat) {
            val message = chat.lastMessage?.message
            if (message != null) {
                val msg = if (message.length < 25) message else message.substring(0, 25) + ".."
                binding.tvMessage.text = msg
            }

            val email = chat.members.find { x -> x != loggedInUser.email }
            binding.tvEmail.text = email
            if (message != null) {
                binding.tvTime.text = getUserCardDate(chat.lastMessage!!.createdAt)
            }

            binding.isOnline.visibility = if (chat.isOnline == true) View.VISIBLE else View.GONE
            binding.isRead.visibility = if (chat.isRead == false) View.VISIBLE else View.GONE
            binding.typing.visibility = if (chat.isTyping == true) View.VISIBLE else View.GONE

            binding.clContainer.setOnClickListener {
                redirectToChat(chat.id)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxUserAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_inbox_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: InboxUserAdapter.ViewHolder, position: Int) {
        holder.databind(chats[position])
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}