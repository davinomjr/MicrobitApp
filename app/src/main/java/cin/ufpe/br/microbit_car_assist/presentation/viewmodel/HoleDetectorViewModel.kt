package cin.ufpe.br.microbit_car_assist.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import cin.ufpe.br.microbit_car_assist.domain.entities.AccelerometerData
import cin.ufpe.br.microbit_car_assist.domain.interactor.HoleDetected
import cin.ufpe.br.microbit_car_assist.domain.interactor.HoleDetector
import com.davinomjr.base.viewmodel.BaseViewModel
import javax.inject.Inject

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 5:38 PM
 */

class HoleDetectorViewModel
    @Inject constructor(val holeDetector: HoleDetector,
                        val holeDetected: HoleDetected)
    : BaseViewModel() {

    var accelerometerData: MutableLiveData<AccelerometerDataView> = MutableLiveData()

    fun handleAccelerometerChange(data: AccelerometerData) {
        holeDetector.execute({it.either(::handleFailure, ::handleHoleDetectedResult)}, data)
    }

    fun handleHoleDetectedResult(result: HoleDetector.HoleDetectorResult){
        if(result.isHole){
            // TODO(ADD HOLE)
            // TODO(LOG)
            //this.accelerometerData.value = AccelerometerDataView(result.data.x,result.data.y,result.data.z, result.data.pitch, result.data.roll)
        }
        else{
            // Nothing to do...
        }
    }

}