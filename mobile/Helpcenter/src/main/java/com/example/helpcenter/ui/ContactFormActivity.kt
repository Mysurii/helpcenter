package com.example.helpcenter.ui

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.helpcenter.R
import com.example.helpcenter.databinding.ActivityContactFormBinding
import com.example.helpcenter.api.Api
import com.example.helpcenter.api.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class ContactFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactFormBinding
    private val apiService: ApiService = Api.createApi()
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSendMessage.setOnClickListener{ onClick() }
        binding.backbutton.setOnClickListener{ finish() }

    }

    private fun onClick(){

        // use this for the email of the currently logged in user.
        val currentUser = "niek.groen@hva.nl"

        val chosenSubject = binding.spnSubjects.selectedItem.toString()
        val messageText = binding.etMessage.text.toString()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Bevestig bericht")
        val dialogLayout = layoutInflater.inflate(R.layout.confirm_send_form, null)
        val dialogSubject = dialogLayout.findViewById<TextView>(R.id.tvFormPopupSubject)
        val dialogMessage = dialogLayout.findViewById<TextView>(R.id.tvFormPopupMessage)

        dialogSubject.text = chosenSubject
        dialogMessage.text = messageText

        if(messageText.isNotBlank()){
            Log.i("Contact form", "[$chosenSubject] $messageText")

            builder.setView(dialogLayout)
            builder.setPositiveButton("Verstuur"){d: DialogInterface, _: Int ->
                try {
                    sendMail(currentUser, chosenSubject,messageText)
                    Toast.makeText(this, "Message sent!", Toast.LENGTH_LONG).show()
                    d.dismiss()
                    finish()
                } catch (_: Throwable) {
                    Toast.makeText(this, "An error occured. Please try again.", Toast.LENGTH_LONG).show()
                }
            }
            builder.setNegativeButton("Terug"){d: DialogInterface, _: Int ->
                d.dismiss()
            }
            builder.show()
        } else {
            Toast.makeText(this, "Please fill in the form.", Toast.LENGTH_LONG).show()
        }

    }

    private fun sendMail(address: String, subject: String, text: String){
        val jsonObject = JSONObject("""{'address':'$address','subject':'$subject','text':'$text'}""")

        mainScope.launch {
            try {
                withTimeout(5_000) {
                    apiService.sendContactform(jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull()))

                }
            } catch (err: Throwable) {
                Log.d("err", err.toString())
            }
        }
    }



}