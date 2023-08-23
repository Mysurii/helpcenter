package com.example.helpcenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helpcenter.R
import com.example.helpcenter.databinding.ItemNewUserBinding

class NewUserAdapter(private val users: List<String>, private val startConversation: (String) -> Unit) :
    RecyclerView.Adapter<NewUserAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ItemNewUserBinding.bind(itemView)

        fun databind(email: String) {
            binding.btnEmail.text = email

            binding.btnEmail.setOnClickListener {
                startConversation(email)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_new_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}