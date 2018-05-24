package cin.ufpe.br.microbit_car_assist.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import cin.ufpe.br.microbit_car_assist.domain.interactor.HoleDetected
import com.davinomjr.base.viewmodel.BaseViewModel
import kotlinx.coroutines.experimental.handleCoroutineException
import javax.inject.Inject

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 12:03 PM
 */

class HolesViewModel
@Inject constructor(private val holeDetected: HoleDetected) : BaseViewModel() {

    var hole: MutableLiveData<HoleView> = MutableLiveData()

    fun addHole(hole: Hole) = holeDetected.execute({ it.either(::handleFailure, ::handleHoleDetected) }, hole)

    fun handleHoleDetected(hole: Hole) {
        this.hole.value = HoleView(hole.latitude,hole.longitude,hole.date)
    }

}