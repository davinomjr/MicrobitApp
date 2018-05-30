package cin.ufpe.br.microbit_car_assist.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.presentation.ui.entities.HoleMarker
import cin.ufpe.br.microbit_car_assist.presentation.ui.util.MessageUtil
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleView
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HolesViewModel
import com.davinomjr.base.ui.BaseFragment
import com.davinomjr.extension.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/28/2018 7:03 PM
 */

abstract class BaseHoleMapFragment : BaseFragment(), OnMapReadyCallback {

    private var TAG = BaseHoleMapFragment::class.java.simpleName
    protected lateinit var holesViewModel: HolesViewModel

    private var mMapReady: Boolean = false
    private lateinit var mMap: GoogleMap
    private lateinit var mClusterManager: ClusterManager<HoleMarker>

    private var delayMarkersMap: Long = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        holesViewModel = viewModel()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView")
        val rootView = inflater!!.inflate(R.layout.fragment_hole_map, container, false)
        val mapFragment = this.childFragmentManager.findFragmentById(R.id.gmap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return rootView
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Log.i(TAG, "onMapReady")
        mMap = googleMap
        setupClusters()
        mMapReady = true
        holesViewModel.getHoles(::showHolesOnMap)

        Observable.interval(delayMarkersMap, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ holesViewModel.getHoles(::showHolesOnMap)})
    }

    private fun setupClusters(){
        mClusterManager = ClusterManager<HoleMarker>(this.activity, mMap)
        mMap.setOnCameraIdleListener(mClusterManager)
        mMap.setOnMarkerClickListener(mClusterManager)
    }

    protected fun addMarker(latitude: Double, longitude: Double, title: String){
        Log.i(TAG, "Adding marker to map")
        val marker = HoleMarker(latitude, longitude, title)
        mClusterManager.addItem(marker)
        moveCamera(LatLng(latitude,longitude), 15f, 1000)
    }

    protected fun moveCamera(latLong: LatLng, zoomLevel: Float = 0f, animationTime: Int = 2000){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel), animationTime, null)
    }

    protected fun showHolesOnMap(holes: List<HoleView>) {
        Log.i(TAG, "showHolesOnMap")
        mClusterManager.clearItems()
        if(mMapReady) {
            if (holes.any()) {
                holes.forEach { hole: HoleView -> addMarker(hole.latitude,hole.longitude,hole.date) }
                moveCamera(LatLng(holes.last().latitude, holes.last().longitude), 15f, 3000)
            } else {
                MessageUtil.showSnack(this.view!!, getString(R.string.no_holes))
            }
        }
        else{
            Log.e(TAG, "Map not ready yet!")
        }
    }
}