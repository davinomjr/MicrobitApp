package cin.ufpe.br.microbit_car_assist.presentation.ui.fragment

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.LiveFolders.INTENT
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton

import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.presentation.data.LocationLiveData
import cin.ufpe.br.microbit_car_assist.presentation.lifecycle.AccelerometerBluetoothObserver
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleDetectorViewModel
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HolesViewModel
import cin.ufpe.br.microbit_car_assist.util.Date
import com.bluetooth.mwoolley.microbitbledemo.MicroBit
import com.davinomjr.base.ui.BaseFragment
import com.davinomjr.extension.viewModel
import kotlinx.android.synthetic.main.fragment_hole_detecting.*
import kotlinx.android.synthetic.main.fragment_hole_detecting.view.*
import org.jetbrains.anko.email
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class HoleDetectingFragment : BaseFragment() {

    private val TAG = "HoleDetectingFragment"

    @Inject lateinit var holeViewModel: HolesViewModel
    @Inject lateinit var holeDetectorViewModel: HoleDetectorViewModel

    private lateinit var holeDetectingObserver: AccelerometerBluetoothObserver
    private lateinit var locationData: LocationLiveData

    private var detecting = false


    // Remove later
    lateinit var data_adapter: ListAdapter

    var holeMode: Boolean = false
    private lateinit var logDir: File
    private var currentStream: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent.inject(this)

        holeViewModel = viewModel()
        holeDetectorViewModel = viewModel()
        locationData = LocationLiveData(this.activity)

        holeDetectorViewModel.accelerometerData.observe(this, Observer { data ->
            var dataText = data.toString()
            dataText += if(holeMode) " Buraco = 1" else " Buraco = 0"

            data_adapter.data.add(dataText)
            data_adapter.notifyDataSetChanged()

            currentStream += "${dataText}\n"
            accelerometerData.smoothScrollToPosition(data_adapter.itemCount)

            holeDetectorViewModel.handleAccelerometerChange(holeDetectorViewModel.AccelerometerDataViewToData(data!!))
        })

        holeDetectorViewModel.lastDetectedHole.observe(this, Observer{hole ->
            // TODO(Mark on map)
        })

        locationData.observe(this, Observer { location ->
            holeDetectorViewModel.lastKnownLocation = location
        })

        holeDetectingObserver = AccelerometerBluetoothObserver(holeDetectorViewModel, this.context)

        val intent = this.activity.getIntent()
        MicroBit.getInstance().microbit_name = intent.getStringExtra("name")
        MicroBit.getInstance().microbit_address = intent.getStringExtra("address")
        MicroBit.getInstance().connection_status_listener = holeDetectorViewModel

        this.lifecycle.addObserver(holeDetectingObserver)

        logDir = File("/DCIM", "Logs_Microbit_Car")
        Log.i(TAG, "TRYING TO CREATE DIR = $logDir")
        logDir.mkdirs()

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater!!.inflate(R.layout.fragment_hole_detecting, container, false)
        rootView.startStopDetecting.setOnClickListener(::handleDetectingButtonClick)
        data_adapter = ListAdapter(mutableListOf())
        rootView.accelerometerData.layoutManager = LinearLayoutManager(rootView.context)
        rootView.accelerometerData.adapter = data_adapter

        rootView.holeButtonMode.setOnClickListener({ t -> holeMode = t.isEnabled })
        return rootView
    }

    fun handleDetectingButtonClick(view: View?){
        if(detecting){
            Log.i(TAG, "stop listening")
            holeDetectingObserver.stopListening()
            startStopDetecting.text = "Start detecting"
            val nowDate: String = Date.now()
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "LogMicrobit_$nowDate.txt")
            file.appendText(currentStream)

            this.activity.email("vhssa@cin.ufpe.br", "Log gerado pelo Microbit Car Assist", currentStream)
        }
        else{
            Log.i(TAG, "start listening")
            currentStream = ""
            holeDetectingObserver.startListening()
            startStopDetecting.text = "Stop detecting"
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

    fun getDevice(position: Int) = data[position]

    class ViewHolder(val containerView: View)
        : RecyclerView.ViewHolder(containerView) {

        fun bind(data: String){
            containerView.findViewById<TextView>(R.id.textView).text = data
        }
    }
}