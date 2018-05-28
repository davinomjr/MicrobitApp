package cin.ufpe.br.microbit_car_assist.util

import android.annotation.SuppressLint
import android.os.Build
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Date

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/24/2018 10:13 PM
 */

class Date {

    companion object {
        @SuppressLint("SimpleDateFormat")
        fun nowAsString(): String {
            val pattern = "dd_MM_yyyy_HH_mm_ss"
            return getDateWithPattern(pattern)
        }

        fun now(): Date {
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC))
            } else{
                Calendar.getInstance().time
            }
        }

        fun toDate(date: String): Date {
            val pattern = "dd_MM_yyyy_HH_mm_ss"
            val format: DateFormat = SimpleDateFormat(pattern)
            return format.parse(date)
        }

        fun nowTime() : String {
            val pattern = "HH:mm:ss.SSS"
            return getDateWithPattern(pattern)
        }

        private fun getDateWithPattern(pattern: String): String {
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))
            } else{
                SimpleDateFormat(pattern).format(Calendar.getInstance().time)
            }
        }
    }
}
