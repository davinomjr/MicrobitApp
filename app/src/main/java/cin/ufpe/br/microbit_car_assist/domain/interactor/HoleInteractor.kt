package cin.ufpe.br.microbit_car_assist.domain.interactor

import cin.ufpe.br.microbit_car_assist.domain.entities.Hole

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/17/2018 9:30 PM
 */

interface HoleInteractor : Interactor {

    interface Callback {
        fun onHolesLoaded(holes: List<Hole>)
        fun onError(error: String)
    }
}