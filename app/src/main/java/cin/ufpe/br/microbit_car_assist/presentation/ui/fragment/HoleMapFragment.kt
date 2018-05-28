package cin.ufpe.br.microbit_car_assist.presentation.ui.fragment


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.presentation.ui.activity.HoleDetectorActivity
import cin.ufpe.br.microbit_car_assist.presentation.ui.entities.HoleMarker
import cin.ufpe.br.microbit_car_assist.presentation.ui.util.ToastUtil
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleDetectorViewModel
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleView
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HolesViewModel
import com.davinomjr.base.ui.BaseFragment
import com.davinomjr.extension.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import javax.inject.Inject
import javax.inject.Singleton


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
        holeDetectorViewModel.lastDetectedHole.observe(this, Observer{ hole -> addHoleOnMap(hole!!)})
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
