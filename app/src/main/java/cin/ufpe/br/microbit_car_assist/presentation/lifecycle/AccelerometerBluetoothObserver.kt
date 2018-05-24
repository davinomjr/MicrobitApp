package cin.ufpe.br.microbit_car_assist.presentation.lifecycle

import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.AccelerometerDataView
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleDetectorViewModel
import com.bluetooth.mwoolley.microbitbledemo.Constants
import com.bluetooth.mwoolley.microbitbledemo.MicroBit
import com.bluetooth.mwoolley.microbitbledemo.Settings
import com.bluetooth.mwoolley.microbitbledemo.Utility
import com.bluetooth.mwoolley.microbitbledemo.bluetooth.BleAdapterService

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 10:00 PM
 */

class AccelerometerBluetoothObserver(val viewModel: HoleDetectorViewModel, val context: Context) : LifecycleObserver {

    private val TAG: String = "AccelerometerBluetoothObserver"

    private lateinit var bluetooth_le_adapter: BleAdapterService

    //// Original implementation, did not botter to refactoring (at this time)
    private val ACCELEROMETER_G_RANGE = 2
    private val ACCELEROMETER_DIVISOR = 512

    private val accel_input = FloatArray(3)
    private var accel_output = FloatArray(3)

    private var exiting = false
    private var accelerometer_period: Int = 0

    private var notifications_on = false
    private var start_time: Long = 0
    private var minute_number: Int = 0
    private var notification_count: Int = 0

    private var serviceInitialized: Boolean = false

    val mServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
                Log.i(TAG, "onServiceConnected")
                Log.i(TAG, "Microbit connected = " + com.bluetooth.mwoolley.microbitbledemo.MicroBit.getInstance().isMicrobit_connected)
                serviceInitialized = true
                notifications_on = false
                start_time = System.currentTimeMillis()
                minute_number = 1
                notification_count = 0

                bluetooth_le_adapter = (service as BleAdapterService.LocalBinder).service
                bluetooth_le_adapter.setActivityHandler(mMessageHandler)
                bluetooth_le_adapter.connect(MicroBit.getInstance().microbit_address)
                bluetooth_le_adapter.readCharacteristic(Utility.normaliseUUID(BleAdapterService.ACCELEROMETERSERVICE_SERVICE_UUID), Utility.normaliseUUID(BleAdapterService.ACCELEROMETERPERIOD_CHARACTERISTIC_UUID))
            }

            override fun onServiceDisconnected(componentName: ComponentName) {}
        }


    val mMessageHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            Log.i(TAG, "handleMessage")
            val bundle: Bundle
            var service_uuid:  String? = ""
            var characteristic_uuid: String? = ""
            var descriptor_uuid: String? = ""
            var b: ByteArray? = null

            when (msg.what) {
                BleAdapterService.GATT_CONNECTED -> {
                    Log.d(TAG, "GATT connected ")
                    bluetooth_le_adapter.discoverServices()
                }
                BleAdapterService.GATT_SERVICES_DISCOVERED -> {
                    Log.d(TAG, "XXXX Services discovered")
                    val slist = bluetooth_le_adapter.supportedGattServices
                    for (svc in slist) {
                        Log.d(Constants.TAG, "UUID=" + svc.uuid.toString().toUpperCase() + " INSTANCE=" + svc.instanceId)
                        MicroBit.getInstance().addService(svc)
                    }
                    MicroBit.getInstance().isMicrobit_services_discovered = true
                }
                BleAdapterService.GATT_CHARACTERISTIC_READ -> {
                    bundle = msg.data
                    service_uuid = bundle.getString(BleAdapterService.PARCEL_SERVICE_UUID)
                    characteristic_uuid = bundle.getString(BleAdapterService.PARCEL_CHARACTERISTIC_UUID)
                    b = bundle.getByteArray(BleAdapterService.PARCEL_VALUE)
                    if (characteristic_uuid!!.equals(Utility.normaliseUUID(BleAdapterService.ACCELEROMETERPERIOD_CHARACTERISTIC_UUID), ignoreCase = true)) {
                        var got_accelerometer_period = false
                        val period_bytes = ByteArray(2)
                        if (b!!.size == 2) {
                            period_bytes[0] = b[0]
                            period_bytes[1] = b[1]
                            got_accelerometer_period = true
                        } else {
                            if (b.size == 1) {
                                period_bytes[0] = b[0]
                                period_bytes[1] = 0x00
                                got_accelerometer_period = true
                            }
                        }
                        if (got_accelerometer_period) {
                            accelerometer_period = Utility.shortFromLittleEndianBytes(period_bytes).toInt()
                            Settings.getInstance().accelerometer_period = accelerometer_period.toShort()
                        }
                    }

                    bluetooth_le_adapter.setNotificationsState(Utility.normaliseUUID(BleAdapterService.ACCELEROMETERSERVICE_SERVICE_UUID), Utility.normaliseUUID(BleAdapterService.ACCELEROMETERDATA_CHARACTERISTIC_UUID), true)
                }
                BleAdapterService.GATT_CHARACTERISTIC_WRITTEN -> {
                    bundle = msg.data
                    service_uuid = bundle.getString(BleAdapterService.PARCEL_SERVICE_UUID)
                    characteristic_uuid = bundle.getString(BleAdapterService.PARCEL_CHARACTERISTIC_UUID)
                }
                BleAdapterService.GATT_DESCRIPTOR_WRITTEN -> {
                    bundle = msg.data
                    service_uuid = bundle.getString(BleAdapterService.PARCEL_SERVICE_UUID)
                    characteristic_uuid = bundle.getString(BleAdapterService.PARCEL_CHARACTERISTIC_UUID)
                    descriptor_uuid = bundle.getString(BleAdapterService.PARCEL_DESCRIPTOR_UUID)
                    if (!exiting) {
                        notifications_on = true
                        start_time = System.currentTimeMillis()
                    } else {
                        notifications_on = false
                    }
                }

                BleAdapterService.NOTIFICATION_OR_INDICATION_RECEIVED -> {
                    bundle = msg.data
                    service_uuid = bundle.getString(BleAdapterService.PARCEL_SERVICE_UUID)
                    characteristic_uuid = bundle.getString(BleAdapterService.PARCEL_CHARACTERISTIC_UUID)
                    b = bundle.getByteArray(BleAdapterService.PARCEL_VALUE)
                    if (characteristic_uuid!!.equals(Utility.normaliseUUID(BleAdapterService.ACCELEROMETERDATA_CHARACTERISTIC_UUID), ignoreCase = true)) {
                        notification_count++
                        if (System.currentTimeMillis() - start_time >= 60000) {
                            notification_count = 0
                            minute_number++
                            start_time = System.currentTimeMillis()
                        }

                        val x_bytes = ByteArray(2)
                        val y_bytes = ByteArray(2)
                        val z_bytes = ByteArray(2)
                        System.arraycopy(b!!, 0, x_bytes, 0, 2)
                        System.arraycopy(b, 2, y_bytes, 0, 2)
                        System.arraycopy(b, 4, z_bytes, 0, 2)
                        val raw_x = Utility.shortFromLittleEndianBytes(x_bytes)
                        val raw_y = Utility.shortFromLittleEndianBytes(y_bytes)
                        val raw_z = Utility.shortFromLittleEndianBytes(z_bytes)

                        // range is -1024 : +1024
                        // Starting with the LED display face up and level (perpendicular to gravity) and edge connector towards your body:
                        // A negative X value means tilting left, a positive X value means tilting right
                        // A negative Y value means tilting away from you, a positive Y value means tilting towards you
                        // A negative Z value means ?

                        accel_input[0] = raw_x / 1000f
                        accel_input[1] = raw_y / 1000f
                        accel_input[2] = raw_z / 1000f

                        accel_output[0] = accel_input[0]
                        accel_output[1] = accel_input[1]
                        accel_output[2] = accel_input[2]


                        var pitch = Math.atan(accel_output[0] / Math.sqrt(Math.pow(accel_output[1].toDouble(), 2.0) + Math.pow(accel_output[2].toDouble(), 2.0)))
                        var roll = Math.atan(accel_output[1] / Math.sqrt(Math.pow(accel_output[0].toDouble(), 2.0) + Math.pow(accel_output[2].toDouble(), 2.0)))

                        //convert radians into degrees
                        pitch = pitch * (180.0 / Math.PI)
                        roll = -1.0 * roll * (180.0 / Math.PI)

                        Log.i(TAG, "accelerometer data changed!")
                        viewModel.accelerometerData.value = AccelerometerDataView(raw_x, raw_y, raw_z, pitch, roll)
                    }
                }
                BleAdapterService.GATT_REMOTE_RSSI -> {
                    bundle = msg.data
                    val rssi = bundle.getInt(BleAdapterService.PARCEL_RSSI)
                }
                BleAdapterService.MESSAGE -> {
                    bundle = msg.data
                    val text = bundle.getString(BleAdapterService.PARCEL_TEXT)
                    //showMsg(Utility.htmlColorRed(text))
                }
            }
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        val gattServiceIntent = Intent(context, BleAdapterService::class.java)
        context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        context.unbindService(mServiceConnection)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){
        if (serviceInitialized && Settings.getInstance().accelerometer_period.toInt() != accelerometer_period) {
            accelerometer_period = Settings.getInstance().accelerometer_period.toInt()
            bluetooth_le_adapter.writeCharacteristic(Utility.normaliseUUID(BleAdapterService.ACCELEROMETERSERVICE_SERVICE_UUID), Utility.normaliseUUID(BleAdapterService.ACCELEROMETERPERIOD_CHARACTERISTIC_UUID), Utility.leBytesFromShort(Settings.getInstance().accelerometer_period))
        }
    }


}