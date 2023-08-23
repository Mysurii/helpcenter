package com.example.helpcenter.ui.chat

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.example.helpcenter.R
import com.example.helpcenter.databinding.ActivityChatBinding
import com.example.helpcenter.repositories.SocketHandler
import com.example.helpcenter.viewmodel.MessageViewModel

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel: MessageViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeData()

        val navController = findNavController(R.id.nav_host_fragment_content_chat)

        val isInboxFragment = intent.extras?.getBoolean("isInboxFragment")

        if (isInboxFragment != null && isInboxFragment) {
            navController.navigate(R.id.action_ChatWithBotFragment_to_InboxFragment)
        }
    }

    private fun observeData() {
        viewModel.connect()
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Wait while loading..")
        progressDialog.setCancelable(false)

        viewModel.isLoading.observe(this, {
            if (it) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        })

        viewModel.errorText.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.closeConnection()
    }
}