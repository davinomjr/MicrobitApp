package cin.ufpe.br.microbit_car_assist.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.location.Location
import cin.ufpe.br.microbit_car_assist.domain.entities.AccelerometerData
import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import cin.ufpe.br.microbit_car_assist.domain.interactor.HoleDetected
import cin.ufpe.br.microbit_car_assist.domain.interactor.HoleDetector
import cin.ufpe.br.microbit_car_assist.presentation.data.LocationLiveData
import cin.ufpe.br.microbit_car_assist.util.Date
import com.davinomjr.base.viewmodel.BaseViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    var lastDetectedHole: MutableLiveData<HoleView> = MutableLiveData()
    var lastKnownLocation: Location? = null

    fun handleAccelerometerChange(data: AccelerometerData) {
        holeDetector.execute({it.either(::handleFailure, ::handleHoleDetectedResult)}, data)
    }

    fun handleHoleDetectedResult(result: HoleDetector.HoleDetectorResult){
        if(result.isHole && lastKnownLocation != null){
            val hole = Hole(lastKnownLocation?.latitude!!, lastKnownLocation?.longitude!!, Date.now())
            holeDetected.execute({it.either(::handleFailure, ::handleHoleAdded)}, hole)
            // TODO(LOG)
            //this.accelerometerData.value = AccelerometerDataView(result.data.x,result.data.y,result.data.z, result.data.pitch, result.data.roll)
        }
        else{
            // Nothing to do...
        }
    }

    fun handleHoleAdded(holeAdded: Hole){
        lastDetectedHole.value = HoleToView(holeAdded)
    }

    fun AccelerometerDataToView(data: AccelerometerData) = AccelerometerDataView(data.x,data.y,data.z,data.roll,data.pitch)
    fun AccelerometerDataViewToData(view: AccelerometerDataView) = AccelerometerData(view.x,view.y,view.z,view.roll,view.pitch)

    fun HoleToView(hole: Hole) = HoleView(hole.latitude,hole.longitude,hole.date)
    fun ViewToHole(view: HoleView) = Hole(view.latitude,view.longitude,view.date)
}