package cin.ufpe.br.microbit_car_assist.storage

import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import com.google.firebase.database.DataSnapshot
import io.reactivex.Observable

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/08/2018 11:16 PM
 */

interface HoleRepository  {

    fun addHole(hole: Hole)
    fun getHoles(): Observable<DataSnapshot>
}