package cin.ufpe.br.microbit_car_assist.presentation.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.presentation.lifecycle.AccelerometerBluetoothObserver
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleDetectorViewModel
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HolesViewModel
import com.bluetooth.mwoolley.microbitbledemo.MicroBit
import com.bluetooth.mwoolley.microbitbledemo.bluetooth.ConnectionStatusListener
import com.davinomjr.base.ui.BaseFragment
import com.davinomjr.extension.viewModel
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class HoleDetectingFragment : BaseFragment(), ConnectionStatusListener {

    private val TAG = "HoleDetectingFragment"

    private lateinit var holeDetectingObserver: AccelerometerBluetoothObserver
    @Inject lateinit var holeViewModel: HolesViewModel
    @Inject lateinit var holeDetectorViewModel: HoleDetectorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent.inject(this)

        holeViewModel = viewModel()
        holeDetectorViewModel = viewModel()

        holeDetectorViewModel.accelerometerData.observe(this, Observer { hole ->
            Log.i(TAG, "Hole detected")
        })

        holeDetectingObserver = AccelerometerBluetoothObserver(holeDetectorViewModel, this.context)

        val intent = this.activity.getIntent()
        MicroBit.getInstance().microbit_name = intent.getStringExtra("name")
        MicroBit.getInstance().microbit_address = intent.getStringExtra("address")
        MicroBit.getInstance().connection_status_listener = this

        this.lifecycle.addObserver(holeDetectingObserver)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_hole_detecting, container, false)
    }


    override fun connectionStatusChanged(new_state: Boolean) {
        if(new_state) Log.i(TAG, "Connection status changed to CONNECTED")
        else Log.i(TAG, "Connection status changed to DISCONNECTED")
    }

    override fun serviceDiscoveryStatusChanged(new_state: Boolean) {

    }
}
