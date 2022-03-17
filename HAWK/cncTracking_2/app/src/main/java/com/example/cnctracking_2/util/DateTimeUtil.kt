package com.example.cnctracking_2.util

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {

    companion object{
        val FORMAT_DATE_TIME_LOGGING = "dd-MM-yyyy"
        val FORMAT_TIME_BOOKING = "HH:mm"

        fun getCurrentDateTime(format: String?, locale: Locale?,time:Long): String
        {
            val calendar = Calendar.getInstance()
            calendar.time = Date(time)
            val sdf = SimpleDateFormat(FORMAT_DATE_TIME_LOGGING)
            sdf.timeZone = TimeZone.getTimeZone("GMT")

            return sdf.format(calendar.time)
        }

        fun getTime(time:Long): String?
        {
            val sdf = SimpleDateFormat(FORMAT_TIME_BOOKING)
            sdf.timeZone = TimeZone.getTimeZone("GMT")
           return sdf.format(Date(time))
        }
    }
}