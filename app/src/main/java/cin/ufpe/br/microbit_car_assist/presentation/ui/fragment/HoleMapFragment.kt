package cin.ufpe.br.microbit_car_assist.presentation.ui.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import cin.ufpe.br.microbit_car_assist.domain.executor.MainThread
import cin.ufpe.br.microbit_car_assist.domain.executor.ThreadExecutor
import cin.ufpe.br.microbit_car_assist.presentation.presenter.HolePresenter
import cin.ufpe.br.microbit_car_assist.presentation.presenter.HolePresenterImpl
import cin.ufpe.br.microbit_car_assist.presentation.ui.ToastUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/**
 * A simple [Fragment] subclass.
 */
class HoleMapFragment : Fragment(), OnMapReadyCallback, HolePresenter.View {

    private lateinit var mPresenter: HolePresenter

    private lateinit var mMap: GoogleMap
    private val TAG: String = "HoleMapFragment"
    private var mMapReady: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        mPresenter = HolePresenterImpl(ThreadExecutor(), MainThread(), this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView")
        val rootView = inflater!!.inflate(R.layout.fragment_hole_map, container, false)
        val mapFragment = this.childFragmentManager.findFragmentById(R.id.gmap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return rootView
    }

    override fun onResume() {
        Log.i(TAG, "onResume")
        super.onResume()
        mPresenter.resume()
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
        mMapReady = true
    }


    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(error: String) {
        ToastUtil.showMessage(context, error)
    }

    override fun showHolesOnMap(holes: List<Hole>) {
        Log.i(TAG, "showHolesOnMap")
        if(mMapReady) {
            if (holes.any()) {
                holes.forEach({ hole: Hole? ->
                    if (hole != null)
                        mMap.addMarker(MarkerOptions().position(LatLng(hole.latitude, hole.longitude)).title(hole.date))
                })

                mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(holes.last()!!.latitude, holes.last()!!.longitude)))
            } else {
                ToastUtil.showMessage(this.context, getString(R.string.no_holes))
            }
        }
        else{
            Log.e(TAG, "Map not ready yet!")
        }
    }
}
