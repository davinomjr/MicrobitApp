package cin.ufpe.br.microbit_car_assist.presentation.ui.fragment

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
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
import cin.ufpe.br.microbit_car_assist.presentation.ui.activity.HoleDetectorActivity
import cin.ufpe.br.microbit_car_assist.presentation.ui.util.MessageUtil
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
import com.bluetooth.mwoolley.microbitbledemo.*
import com.davinomjr.base.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 */
class MainFragment : BaseFragment(), ScanResultsConsumer {

    lateinit var ble_device_list_adapter: ListAdapter
    lateinit var ble_scanner: BleScanner
    var ble_scanning: Boolean = false
    val DEVICE_NAME_START: String = "BBC micro"
    private val PERMISSIONS_CODE = 15
    private var device_count: Int = 0
    private val SCAN_TIMEOUT: Long = 30000

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater!!.inflate(R.layout.fragment_main, container, false)
        Settings.getInstance().restore(this.context)

        checkPermissions()

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

        showPairingRationale()
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        this.context.unregisterReceiver(broadcastReceiver)
    }

    fun showPairingRationale(){
        MessageUtil.showAlert(this.activity, getString(R.string.microbit_pairing_request), {})
    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            val action: String = intent!!.action
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                when (device.bondState) {
                    BluetoothDevice.BOND_NONE -> showMessage("Device not paired successfully")
                    BluetoothDevice.BOND_BONDING -> showMessage("Pairing in progress")
                    BluetoothDevice.BOND_BONDED -> showMessage("Paired successfully")
                }
            }
        }
    }

    fun showMessage(message: String) {
        MessageUtil.showSnack(this.view!!, message)
    }

    fun hasNecessaryPermissions() = hasPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE))

    fun hasPermissions(permissions: Array<String>) : Boolean {
        for(permission: String in permissions){
            if(ContextCompat.checkSelfPermission(this.context, permission) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }

        return true
    }

    fun checkPermissions() : Boolean {
        val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(hasPermissions(permissions)){
            return true
        }

        requestPermissions(permissions, PERMISSIONS_CODE)
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionsResult")
        when (requestCode) {
            PERMISSIONS_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showMessage(getString(R.string.permission_error))
                }
            }
        }
    }


    private fun scan(view: View?) {
        if (!ble_scanner.isScanning) {
            device_count = 0
            if (hasNecessaryPermissions()) {
                scanButton.text = getString(R.string.stop_scanning)
                startScan()
            }
            else{
                checkPermissions()
            }
        } else {
            scanButton.text = getString(R.string.start_scanning)
            ble_scanner.stopScanning()
            showMessage(getString(R.string.scanning_stopped))
        }
    }


    fun startScan() {
        showMessage(getString(R.string.scanning_started))
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
