package cin.ufpe.br.microbit_car_assist.domain.entities

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 5:58 PM
 */

data class AccelerometerData(val x: Float, val y: Float, val z: Float, val pitch: Double, val roll: Double, var isHole: Boolean = false)