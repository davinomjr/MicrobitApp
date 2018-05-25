package cin.ufpe.br.microbit_car_assist.util

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
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
        fun now(): String {
            val pattern = "dd_MM_yyyy_HH_mm_ss"
            return getDateWithPattern(pattern)
        }

        fun nowTime() : String {
            val pattern = "HH:mm:ss.SSS"
            return getDateWithPattern(pattern)
        }

        private fun getDateWithPattern(pattern: String): String {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))
            }
            else{
                return SimpleDateFormat(pattern).format(Calendar.getInstance().time)
            }
        }
    }

}