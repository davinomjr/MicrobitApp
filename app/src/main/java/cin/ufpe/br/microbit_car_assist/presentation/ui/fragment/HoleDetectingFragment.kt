package cin.ufpe.br.microbit_car_assist.presentation.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.presentation.data.LocationLiveData
import cin.ufpe.br.microbit_car_assist.presentation.lifecycle.AccelerometerBluetoothObserver
import cin.ufpe.br.microbit_car_assist.presentation.ui.activity.HoleDetectorActivity
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleDetectorViewModel
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HolesViewModel
import cin.ufpe.br.microbit_car_assist.util.Date
import com.bluetooth.mwoolley.microbitbledemo.MicroBit
import com.davinomjr.base.ui.BaseFragment
import com.davinomjr.extension.viewModel
import kotlinx.android.synthetic.main.fragment_hole_detecting.*
import kotlinx.android.synthetic.main.fragment_hole_detecting.view.*
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 */
class HoleDetectingFragment : BaseFragment() {

    private val TAG = "HoleDetectingFragment"

    private lateinit var holeViewModel: HolesViewModel
    private lateinit var holeDetectorViewModel: HoleDetectorViewModel

    private lateinit var holeDetectingObserver: AccelerometerBluetoothObserver
    private lateinit var locationData: LocationLiveData

    private var detecting = false

    lateinit var data_adapter: ListAdapter
    private var currentStream: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent.inject(this)

        holeViewModel = viewModel()
        holeDetectorViewModel = viewModel()

        locationData = LocationLiveData(this.activity)

        holeDetectingObserver = AccelerometerBluetoothObserver(holeDetectorViewModel, this.context)
        this.lifecycle.addObserver(holeDetectingObserver)

        configObservers()

        val intent = this.activity.getIntent()
        MicroBit.getInstance().microbit_name = intent.getStringExtra("name")
        MicroBit.getInstance().microbit_address = intent.getStringExtra("address")
        MicroBit.getInstance().connection_status_listener = holeDetectorViewModel
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater!!.inflate(R.layout.fragment_hole_detecting, container, false)
        rootView.startStopDetecting.setOnClickListener(::handleDetectingButtonClick)
        data_adapter = ListAdapter(mutableListOf())
        rootView.accelerometerData.layoutManager = LinearLayoutManager(rootView.context)
        rootView.accelerometerData.adapter = data_adapter
        return rootView
    }

    fun configObservers(){
        holeDetectorViewModel.accelerometerData.observe(this, Observer { data ->
            val dataText = data.toString()
            Log.i(TAG, "Detected acceelerometer change: $dataText")

//            data_adapter.data.add(dataText)
//            data_adapter.notifyDataSetChanged()
//            accelerometerData.smoothScrollToPosition(data_adapter.itemCount)

            currentStream += "$dataText\n"

            holeDetectorViewModel.handleAccelerometerChange(holeDetectorViewModel.AccelerometerDataViewToData(data!!))
        })

        locationData.observe(this, Observer { location ->
            holeDetectorViewModel.lastKnownLocation = location
        })
    }

    fun handleDetectingButtonClick(view: View?){
        if(detecting){
            Log.i(TAG, "Stopped listening")
            holeDetectingObserver.stopListening()
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
        }

        detecting = !detecting
    }
}


class ListAdapter(var data: MutableList<String>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun contains(data: String) = data.contains(data)

    class ViewHolder(val containerView: View)
        : RecyclerView.ViewHolder(containerView) {

        fun bind(data: String){
            containerView.findViewById<TextView>(R.id.textView).text = data
        }
    }
}