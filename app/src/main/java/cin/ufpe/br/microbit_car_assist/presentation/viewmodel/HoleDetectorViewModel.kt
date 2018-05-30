package cin.ufpe.br.microbit_car_assist.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.location.Location
import android.util.Log
import cin.ufpe.br.microbit_car_assist.domain.entities.AccelerometerData
import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import cin.ufpe.br.microbit_car_assist.domain.interactor.HoleDetected
import cin.ufpe.br.microbit_car_assist.domain.interactor.HoleDetector
import cin.ufpe.br.microbit_car_assist.presentation.ui.entities.HoleLocation
import cin.ufpe.br.microbit_car_assist.util.Date
import com.bluetooth.mwoolley.microbitbledemo.bluetooth.ConnectionStatusListener
import com.davinomjr.base.viewmodel.BaseViewModel
import org.joda.time.Interval
import java.time.Duration
import java.time.Period
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 5:38 PM
 */

class HoleDetectorViewModel
    @Inject constructor(val holeDetector: HoleDetector,
                        val holeDetected: HoleDetected)
    : BaseViewModel(),
      ConnectionStatusListener{

    private val TAG: String = HoleDetectorViewModel::class.java.simpleName

    var accelerometerData: MutableLiveData<AccelerometerDataView> = MutableLiveData()
    var lastDetectedHole: MutableLiveData<HoleView> = MutableLiveData()
    var lastKnownLocation: HoleLocation? = null

    var lastDetectedHoleDate: java.util.Date = Date.now()

    fun handleAccelerometerChange(data: AccelerometerData) {
        holeDetector.execute({it.either(::handleFailure, ::handleHoleDetectedResult)}, data)
    }

    fun handleHoleDetectedResult(result: HoleDetector.HoleDetectorResult){
        if(result.isHole && lastKnownLocation != null && notDuplicatedHole()){
            if(lastKnownLocation == null){
                Log.i(TAG, "Hole detected but could not generate results, as there was no location data present.")
                return
            }

            Log.i(TAG, "Hole detected!")
            lastDetectedHoleDate = Date.now()
            val hole = Hole(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude, Date.nowAsString())
            holeDetected.execute({it.either(::handleFailure, ::handleHoleAdded)}, hole)
        }
        else{
            // Nothing to do...
        }
    }

    fun notDuplicatedHole(): Boolean {
        val now = Date.now()
        val secondsSince = Interval(lastDetectedHoleDate.time, now.time).toDuration().standardSeconds
        return secondsSince >= 2
    }

    fun handleHoleAdded(holeAdded: Hole){
        Log.i(TAG, "handleHoleAdded")
        lastDetectedHole.value = HoleToView(holeAdded)
        Log.i(TAG, "HAS OBSERVERS = ${hasObservers()}")
    }

    fun hasObservers() : String{
        return lastDetectedHole.hasObservers().toString()
    }


    override fun connectionStatusChanged(new_state: Boolean) {
        if(new_state) Log.i(TAG, "Connection status changed to CONNECTED")
        else Log.i(TAG, "Connection status changed to DISCONNECTED")
    }

    override fun serviceDiscoveryStatusChanged(new_state: Boolean) { }

    fun AccelerometerDataToView(data: AccelerometerData) = AccelerometerDataView(data.accel_x,data.accel_y,data.accel_z,data.roll,data.pitch)
    fun AccelerometerDataViewToData(view: AccelerometerDataView) = AccelerometerData(view.x,view.y,view.z,view.roll,view.pitch)

    fun HoleToView(hole: Hole) = HoleView(hole.latitude,hole.longitude,hole.date)
    fun ViewToHole(view: HoleView) = Hole(view.latitude,view.longitude,view.date)
}