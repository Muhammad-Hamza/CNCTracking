package com.example.cnctracking_2.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {

    companion object{
        val FORMAT_DATE_TIME_LOGGING = "MM-dd-yyyy"
        val FORMAT_TIME_BOOKING = "hh:mm a"

        fun getCurrentDateTime(format: String?, locale: Locale?,time:Long): String
        {
            val calendar = Calendar.getInstance()
            calendar.time = Date(time)
            return SimpleDateFormat(format, locale).format(calendar.time)
        }

        fun getDate(dateStr: String?, format: String?): Date?
        {
            var date: Date? = null
            val formatter = SimpleDateFormat(format)
            try
            {
                date = formatter.parse(dateStr)
            }
            catch (e: ParseException)
            {
                e.printStackTrace()
            }
            return date
        }
    }

}