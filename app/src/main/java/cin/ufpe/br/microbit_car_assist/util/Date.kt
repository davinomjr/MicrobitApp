package cin.ufpe.br.microbit_car_assist.util

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/24/2018 10:13 PM
 */

class Date {

    companion object {
        @SuppressLint("SimpleDateFormat")
        fun now(): String {
            val pattern = "dd_MM_yyyy_HH_mm_ss"
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))
            }
            else{
                return SimpleDateFormat(pattern).format(Date())
            }
        }
    }

}