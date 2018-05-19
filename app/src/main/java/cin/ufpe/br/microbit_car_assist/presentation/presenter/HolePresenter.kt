package cin.ufpe.br.microbit_car_assist.presentation.presenter

import cin.ufpe.br.microbit_car_assist.domain.entities.Hole

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/18/2018 9:14 PM
 */

interface HolePresenter : Presenter{

    interface View : BaseView {
        fun showHolesOnMap(holes: List<Hole>)
    }

}
