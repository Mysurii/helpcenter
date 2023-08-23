package com.example.helpcenter

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.helpcenter.models.ChatItem
import com.example.helpcenter.repositories.ResponseRepository
import com.example.helpcenter.utils.Intents.START
import kotlinx.coroutines.*
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Test
import java.util.*

@RunWith(AndroidJUnit4::class)
class ResponseRepositoryTest {
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val responseRepository = ResponseRepository()
}