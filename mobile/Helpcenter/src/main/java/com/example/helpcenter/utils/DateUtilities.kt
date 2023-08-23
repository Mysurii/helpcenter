package com.example.helpcenter.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtilities {


    /**
     * Formats the given date to hh:mm format
     * @param dateTime the date that needs to be formatted
     */
    fun getTimeFromLocalDateTime(dateTime: Date): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        return sdf.format(dateTime)
    }

    /**
     * Formats the given date to a dd/mm format. If the date is today,
     * then it will return 'Vandaag'
     * @param dateTime the date that needs to be formatted
     */
    fun getDateAsHumanReadableString(dateTime: Date): String {
        if (isSameDay(dateTime, Date())) {
            return "Vandaag"
        }

        val simpleDateFormat = SimpleDateFormat("dd MMMM", Locale.ENGLISH)
        return simpleDateFormat.format(dateTime)
    }

    /**
     * Checks whether the two provided dates are the of the same day.
     * Returns true if it is
     * @param date1
     * @param date2
     */
    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
    }

    fun getUserCardDate(date: Date): String {
        val today = Date()
        val timeFormatter = SimpleDateFormat("hh:mm", Locale.ENGLISH)
        val dayFormatter = SimpleDateFormat("EEEE", Locale.ENGLISH)

        if (isSameDay(today, date)) {
            return timeFormatter.format(date)
        }

        val ms = date.time - Calendar.getInstance().timeInMillis
        val days = TimeUnit.MILLISECONDS.toDays(ms)

        if (days < 7) {
            return dayFormatter.format(date)
        }

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return dateFormatter.format(date)
    }

}