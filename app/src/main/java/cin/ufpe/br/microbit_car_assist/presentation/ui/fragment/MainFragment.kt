package cin.ufpe.br.microbit_car_assist.presentation.ui.fragment

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import cin.ufpe.br.microbit_car_assist.presentation.ui.activity.HoleDetectorActivity
import cin.ufpe.br.microbit_car_assist.storage.Database
import com.bluetooth.mwoolley.microbitbledemo.Constants.TAG
import com.bluetooth.mwoolley.microbitbledemo.Settings
import com.bluetooth.mwoolley.microbitbledemo.bluetooth.BleScanner
import com.bluetooth.mwoolley.microbitbledemo.bluetooth.BleScannerFactory
import com.bluetooth.mwoolley.microbitbledemo.bluetooth.ScanResultsConsumer
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

import com.bluetooth.mwoolley.microbitbledemo.*


/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment(), ScanResultsConsumer {

    lateinit var ble_device_list_adapter: ListAdapter
    lateinit var ble_scanner: BleScanner
    var ble_scanning: Boolean = false
    val DEVICE_NAME_START: String = "BBC micro"

    private val ACCESS_COARSE_LOCATION_CODE = 123
    private var device_count: Int = 0
    private val SCAN_TIMEOUT: Long = 30000


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater!!.inflate(R.layout.fragment_main, container, false)
        Settings.getInstance().restore(this.context)

        rootView.scanButton.setOnClickListener({ view: View? -> scan(view) })
        ble_device_list_adapter = ListAdapter(mutableListOf(), { device: BluetoothDevice ->
            processItemClick(device)
        })

        rootView.bluetoothDevices.layoutManager = LinearLayoutManager(rootView.context)
        rootView.bluetoothDevices.adapter = ble_device_list_adapter

        this.context.registerReceiver(broadcastReceiver, IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED))

        ble_scanner = BleScannerFactory.getBleScanner(this.context)
        ble_scanner.device_name_start = DEVICE_NAME_START
        ble_scanner.isSelect_bonded_devices_only = true

        setHoleListener()

        return rootView
    }


    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            val action: String = intent!!.action
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                when (device.bondState) {
                    BluetoothDevice.BOND_NONE -> showMessage("Device not pairead successfully")
                    BluetoothDevice.BOND_BONDING -> showMessage("Pairing in progress")
                    BluetoothDevice.BOND_BONDED -> showMessage("Paired successfully")
                }
            }
        }
    }

    fun showMessage(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
    }

    fun checkPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    ACCESS_COARSE_LOCATION_CODE)
            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            ACCESS_COARSE_LOCATION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showMessage("The application cannot run properly unless the requested permission is granted. Please try again.")
                }
            }
        }
    }

    private fun setHoleListener(){
        val holeListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                Log.i(TAG, "Hole added")
            }

        }

        Database.it.addChildEventListener(holeListener)
    }

    private fun scan(view: View?) {
        if (!ble_scanner.isScanning) {
            device_count = 0
            if (checkPermission()) {
                startScan()
            }
        } else {
            ble_scanner.stopScanning()
            showMessage("Stopped scanning")

        }
    }


    fun startScan() {
        showMessage("Scanning for Microbit devices nearby...")
        ble_scanner.startScanning(this, SCAN_TIMEOUT)
    }

    override fun scanningStarted() {
        ble_scanning = true
    }

    override fun scanningStopped() {
        ble_scanning = false
    }

    override fun candidateBleDevice(device: BluetoothDevice, scan_record: ByteArray, rssi: Int) {
        Log.i(TAG, "Found microbit device")
        if(!ble_device_list_adapter.contains(device)) {
            ble_device_list_adapter.ble_devices.add(device)
            ble_device_list_adapter.notifyDataSetChanged()
            device_count++
        }
    }


    private fun processItemClick(device: BluetoothDevice) {
        Log.i(TAG, "clicked")
        if (ble_scanning) {
            ble_scanning = false
            ble_scanner.stopScanning()
        }

        if (device.bondState == BluetoothDevice.BOND_NONE && Settings.getInstance().isFilter_unpaired_devices) {
            device.createBond()
        }

        val microbit: MicroBit = MicroBit.getInstance()
        microbit.bluetooth_device = device

        val intent = Intent(this.context, HoleDetectorActivity::class.java)
        intent.putExtra("name", device.name)
        intent.putExtra("address", device.address)
        startActivity(intent)
    }


    class ListAdapter(var ble_devices: MutableList<BluetoothDevice>,
                      val itemClick: (BluetoothDevice) -> Unit)
        : RecyclerView.Adapter<ListAdapter.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(ble_devices[position], itemClick)
        }

        override fun getItemCount(): Int = ble_devices.size

        fun contains(device: BluetoothDevice) = ble_devices.contains(device)

        fun getDevice(position: Int) = ble_devices[position]

        class ViewHolder(val containerView: View)
            : RecyclerView.ViewHolder(containerView) {

            fun bind(bleDevice: BluetoothDevice, itemClick: (BluetoothDevice) -> Unit){
                containerView.findViewById<TextView>(R.id.textView).text = bleDevice.name
                containerView.findViewById<TextView>(R.id.bdaddr).text = bleDevice.address
                itemView.setOnClickListener({ itemClick(bleDevice) })
            }
        }
    }
}
