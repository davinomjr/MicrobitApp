package cin.ufpe.br.microbit_car_assist.domain.entities

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/04/2018 3:04 PM
 */

class Hole(){

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var date: String = ""
    var id: String = ""

    constructor(latitude: Double, longitude: Double, date: String) : this() {
        this.latitude = latitude
        this.longitude = longitude
        this.date = date
        this.id = "${latitude.toString().replace('.', '*')}_${longitude.toString().replace('.', '*')}_${this.date}"
    }
}