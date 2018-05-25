package cin.ufpe.br.microbit_car_assist.presentation.viewmodel

import cin.ufpe.br.microbit_car_assist.domain.entities.Hole

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 3:53 PM
 */

data class HoleView(val latitude: Double, val longitude: Double, val date: String){

    fun toHole() = Hole(latitude,longitude,date)
}