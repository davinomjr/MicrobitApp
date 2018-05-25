package cin.ufpe.br.microbit_car_assist.presentation.viewmodel

import cin.ufpe.br.microbit_car_assist.util.Date

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 6:00 PM
 */

class AccelerometerDataView(val x: Float, val y: Float, val z: Float, val pitch: Double, val roll: Double) {

    override fun toString(): String {
        return "${Date.nowTime()} az = $z"
    }
}