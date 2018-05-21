package cin.ufpe.br.microbit_car_assist.domain.executor

import cin.ufpe.br.microbit_car_assist.domain.interactor.AbsInteractor
import cin.ufpe.br.microbit_car_assist.domain.interactor.Interactor

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/17/2018 7:03 PM
 */

interface Executor {
    fun execute(interactor: AbsInteractor)
}