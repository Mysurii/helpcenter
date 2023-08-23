package com.example.helpcenter

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.helpcenter.utils.DateUtilities.getDateAsHumanReadableString
import com.example.helpcenter.utils.DateUtilities.isSameDay
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Test
import java.util.*

@RunWith(AndroidJUnit4::class)
class DateTest {

    @Test
    fun isSameDay() {
        val date1 = Date(2021, 10, 10)
        val date2 = Date(2021, 10, 11)
        val date3 = Date(2021, 10, 10)

        assertFalse(isSameDay(date1, date2))
        assertTrue(isSameDay(date1, date3))

    }

    @Test
    fun humanReadableDate() {
        val today = Date()
        val date1 = Date(2021, 10, 10)

        assertEquals(getDateAsHumanReadableString(today), "Vandaag")
        assertEquals(getDateAsHumanReadableString(date1), "10 November")
    }
}