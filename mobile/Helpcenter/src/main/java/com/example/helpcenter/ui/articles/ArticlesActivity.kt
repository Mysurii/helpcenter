package com.example.helpcenter.ui.articles

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.helpcenter.R
import com.example.helpcenter.databinding.ActivityArticlesBinding
import com.example.helpcenter.viewmodel.ArticleViewModel

class ArticlesActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityArticlesBinding
    private val viewModel: ArticleViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment_content_articles)

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Wait while loading..")
        progressDialog.setCancelable(false)

        viewModel.fetchArticles()

        viewModel.isLoading.observe(this, {
            if (it) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        })

        // If the backwards arrow button is pressed, it goes back to the previous fragment/activity
        binding.backbutton.setOnClickListener {
            val currentDestination = navController.currentDestination
            if (currentDestination != null) {
                if (currentDestination.label == getString(R.string.first_fragment_label)) {
                    finish()
                } else {
                    navController.popBackStack()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_articles)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}