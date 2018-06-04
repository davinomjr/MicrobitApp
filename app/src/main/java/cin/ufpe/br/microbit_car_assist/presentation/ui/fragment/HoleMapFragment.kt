package cin.ufpe.br.microbit_car_assist.presentation.ui.fragment


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.presentation.ui.util.MessageUtil

import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleDetectorViewModel
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleView
import com.davinomjr.extension.viewModel
import com.google.android.gms.maps.model.LatLng


/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 */
class HoleMapFragment : BaseHoleMapFragment() {

    lateinit var holeDetectorViewModel: HoleDetectorViewModel

    private val TAG: String = HoleMapFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        holeDetectorViewModel = viewModel()
        holeDetectorViewModel.lastDetectedHole.observe(this, Observer{ hole ->
            addHoleOnMap(hole!!)
            MessageUtil.showAlert(this.context, getString(R.string.hole_detected))
        })
    }

    override fun onResume() {
        Log.i(TAG, "onResume")
        super.onResume()
    }

    fun addHoleOnMap(hole: HoleView){
        Log.i(TAG, "addHoleOnMap")
        addMarker(hole.latitude,hole.longitude, hole.date)
        moveCamera(LatLng(hole.latitude,hole.longitude), 15f)
    }
}
