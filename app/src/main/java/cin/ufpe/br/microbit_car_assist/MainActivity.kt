package cin.ufpe.br.microbit_car_assist

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import cin.ufpe.br.microbit_car_assist.domain.Hole
import cin.ufpe.br.microbit_car_assist.firebase.Database
import com.bluetooth.mwoolley.microbitbledemo.Settings
import com.bluetooth.mwoolley.microbitbledemo.bluetooth.BleScanner
import com.bluetooth.mwoolley.microbitbledemo.bluetooth.BleScannerFactory
import com.bluetooth.mwoolley.microbitbledemo.bluetooth.ScanResultsConsumer
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity(), ScanResultsConsumer {

    lateinit var ble_device_list_adapter: ListAdapter
    lateinit var ble_scanner: BleScanner
    var ble_scanning: Boolean = false
    val DEVICE_NAME_START: String = "BBC Micro"

    private val ACCESS_COARSE_LOCATION_CODE = 123
    private var device_count: Int = 0
    private val SCAN_TIMEOUT: Long = 30000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Device List"
        Settings.getInstance().restore(this)

        scanButton.setOnClickListener({ view: View? -> addItem(view) })

        //scanButton.setOnClickListener({ view: View? -> scan(view) })
//        ble_device_list_adapter = ListAdapter(mutableListOf(), { device: BluetoothDevice ->
//            processItemClick(device)
//        })

        ble_device_list_adapter = ListAdapter(mutableListOf(), { text: String ->
            processItemClick(text)
        })

        bluetoothDevices.layoutManager = LinearLayoutManager(this)
        bluetoothDevices.adapter = ble_device_list_adapter

        registerReceiver(broadcastReceiver, IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED))

        ble_scanner = BleScannerFactory.getBleScanner(this.applicationContext)
        ble_scanner.device_name_start = DEVICE_NAME_START
        ble_scanner.isSelect_bonded_devices_only = true


        Database.configure()
        setHoleListener()
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun checkPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
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

    private fun addItem(view: View?){
        lateinit var fakeHole: Hole
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fakeHole = Hole(-8.1139178, -34.8976436, Date.from(Instant.now()))
        }
        else{
            fakeHole = Hole(-8.1139178, -34.8976436, Calendar.getInstance().time)
        }

        val id: String = fakeHole.id()
        Database.it().child(id).setValue(fakeHole)
    }

    private fun setHoleListener(){
        val holeListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented")
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented")
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                if(dataSnapshot!!.exists()){
                    val hole = dataSnapshot.getValue(Hole::class.java)
                    showMessage(hole!!.latitude.toString())
                    ble_device_list_adapter.numbers.add(hole!!.id())
                    ble_device_list_adapter.notifyDataSetChanged()
                }
            }

        }

        Database.it().addChildEventListener(holeListener)
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
        runOnUiThread({
            ble_device_list_adapter.notifyDataSetChanged()
        })

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
        runOnUiThread {
//            ble_device_list_adapter.ble_devices.add(device)
//            ble_device_list_adapter.notifyDataSetChanged()
//            device_count++
        }
    }

    private fun processItemClick(text: String) {
        showMessage("Item clicked: ${text}")
    }

//
//    private fun processItemClick(device: BluetoothDevice) {
//        if (ble_scanning) {
//            ble_scanning = false
//            ble_scanner.stopScanning()
//        }
//
//        if (device.bondState == BluetoothDevice.BOND_NONE && Settings.getInstance().isFilter_unpaired_devices) {
//            device.createBond()
//            showMessage("Pairing Microbit ${device.name} now")
//            return
//        }
//
//        unregisterReceiver(broadcastReceiver)
//        val microbit: MicroBit = MicroBit.getInstance()
//        microbit.bluetooth_device = device
//
//        // startOtherActivity
//        showMessage("Starting other activity...")
//    }
//
    class ListAdapter(var numbers: MutableList<String>,
                      private val itemClick: (String) -> Unit)
        : RecyclerView.Adapter<ListAdapter.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return numbers.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(numbers[position], itemClick)
        }


        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

            fun bind(item: String, itemClick: (String) -> Unit) {
                with(item) {
                    view.findViewById<TextView>(R.id.textView).text = this
                    view.setOnClickListener({ itemClick(this) })
                }
            }
        }
    }
}


//    class ListAdapter(var ble_devices: MutableList<BluetoothDevice>,
//                      val itemClick: (BluetoothDevice) -> Unit)
//        : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
//
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
//            return ViewHolder(view)
//        }
//
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.bind(ble_devices[position], itemClick)
//        }
//
//        override fun getItemCount(): Int = ble_devices.size
//
//        fun contains(device: BluetoothDevice) = ble_devices.contains(device)
//
//        fun getDevice(position: Int) = ble_devices[position]
//
//        class ViewHolder(val containerView: View)
//            : RecyclerView.ViewHolder(containerView) {
//
//            fun bind(bleDevice: BluetoothDevice, itemClick: (BluetoothDevice) -> Unit){
//                containerView.findViewById<TextView>(R.id.textView).text = bleDevice.name
//                containerView.findViewById<TextView>(R.id.bdaddr).text = bleDevice.address
//                itemView.setOnClickListener({ itemClick(bleDevice) })
//            }
//
//        }
//    }
//}