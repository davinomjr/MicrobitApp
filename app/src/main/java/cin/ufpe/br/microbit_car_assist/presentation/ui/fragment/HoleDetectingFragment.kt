package cin.ufpe.br.microbit_car_assist.presentation.ui.fragment

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.transition.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.presentation.data.LocationLiveData
import cin.ufpe.br.microbit_car_assist.presentation.lifecycle.AccelerometerBluetoothObserver
import cin.ufpe.br.microbit_car_assist.presentation.ui.entities.HoleLocation
import cin.ufpe.br.microbit_car_assist.presentation.ui.util.MessageUtil
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleDetectorViewModel
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HolesViewModel
import cin.ufpe.br.microbit_car_assist.util.Date
import com.bluetooth.mwoolley.microbitbledemo.MicroBit
import com.davinomjr.base.ui.BaseFragment
import com.davinomjr.extension.viewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_hole_detecting.*
import kotlinx.android.synthetic.main.fragment_hole_detecting.view.*
import org.jetbrains.anko.progressDialog
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 12:41 PM
 */
class HoleDetectingFragment : BaseFragment() {

    private val TAG = "HoleDetectingFragment"

    private lateinit var holeViewModel: HolesViewModel
    private lateinit var holeDetectorViewModel: HoleDetectorViewModel

    private lateinit var holeDetectingObserver: AccelerometerBluetoothObserver
    private lateinit var locationData: LocationLiveData

    private var detecting = false
    private var currentStream: String = ""

    lateinit var dialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent.inject(this)

        holeViewModel = viewModel()
        holeDetectorViewModel = viewModel()

        locationData = LocationLiveData(this.activity)

        holeDetectingObserver = AccelerometerBluetoothObserver(holeDetectorViewModel, this.context)
        this.lifecycle.addObserver(holeDetectingObserver)

        setupObservers()

        val intent = this.activity.getIntent()
        MicroBit.getInstance().microbit_name = intent.getStringExtra("name")
        MicroBit.getInstance().microbit_address = intent.getStringExtra("address")
        MicroBit.getInstance().connection_status_listener = holeDetectorViewModel
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater!!.inflate(R.layout.fragment_hole_detecting, container, false)
        rootView.startStopDetecting.setOnClickListener(::handleDetectingButtonClick)
        return rootView
    }

    fun setupObservers(){
        holeDetectorViewModel.accelerometerData.observe(this, Observer { data ->
            val dataText = data.toString()
            Log.i(TAG, "Detected accelerometer change: $dataText")
            currentStream += "$dataText\n"
            holeDetectorViewModel.handleAccelerometerChange(holeDetectorViewModel.AccelerometerDataViewToData(data!!))
        })

        locationData.observe(this, Observer { location ->
            if(location != null) holeDetectorViewModel.lastKnownLocation = HoleLocation(location.latitude,location.longitude)
        })
    }

    fun handleDetectingButtonClick(view: View?){
        if(detecting){
            Log.i(TAG, "Stopped listening")
            MessageUtil.showSnack(this.view!!, getString(R.string.detection_stopped))
            holeDetectingObserver.stopListening()
            spin_loading.visibility = View.INVISIBLE
            startStopDetecting.text = getString(R.string.start_detecting)
            val nowDate: String = Date.nowAsString()
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "LogMicrobit_$nowDate.txt")
            file.appendText(currentStream)
            currentStream = ""
        }
        else{
            Log.i(TAG, "Started listening")
            currentStream = ""
            holeDetectingObserver.startListening()
            startStopDetecting.text = getString(R.string.stop_detecting)

            dialog = ProgressDialog(this.activity)
            dialog.setCancelable(false)
            dialog.setMessage(getString(R.string.wait_time_detect))
            dialog.show()
            Observable.timer(14, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        dialog.cancel()
                        MessageUtil.showSnack(this.view!!, getString(R.string.detection_started))
                        spin_loading.visibility = View.VISIBLE
                    })
        }

        detecting = !detecting
    }
}