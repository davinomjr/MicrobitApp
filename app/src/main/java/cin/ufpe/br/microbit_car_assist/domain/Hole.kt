package cin.ufpe.br.microbit_car_assist.domain

import android.os.Build
import android.support.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/04/2018 3:04 PM
 */

class Hole(){

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var date: Date = Calendar.getInstance().time

    constructor(latitude: Double, longitude: Double, date: Date) : this() {
        this.latitude = latitude
        this.longitude = longitude
        this.date = date
    }

    fun id() : String{
        return "${latitude.toString().replace('.', '*')}_${longitude.toString().replace('.', '*')}_${ SimpleDateFormat("dd_MM_yy_HH_mm_ss").format(date)}"
    }
}