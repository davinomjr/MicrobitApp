package cin.ufpe.br.microbit_car_assist.presentation.data

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationListener
import android.os.Bundle
import android.util.Log
import cin.ufpe.br.microbit_car_assist.App
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/24/2018 9:21 PM
 */

class LocationLiveData(val context: Context)
    : LiveData<Location>(),
                         LocationListener,
                         GoogleApiClient.ConnectionCallbacks,
                         GoogleApiClient.OnConnectionFailedListener
{

    private val TAG = LocationLiveData::class.java.simpleName
    private var googleApiClient: GoogleApiClient = GoogleApiClient.Builder(context, this, this)
                                                                  .addApi(LocationServices.API)
                                                                  .build()

    override fun onActive() {
        super.onActive()
        googleApiClient.connect()
        Log.i(TAG, "googleApiCLient ON")
    }

    override fun onInactive() {
        super.onInactive()
        Log.i(TAG, "googleApiCLient ON")
        if (googleApiClient.isConnected) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
        }

        googleApiClient.disconnect()
    }

    override fun onLocationChanged(location: Location?) {
        Log.i(TAG, "Setting location")
        value = location
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        value = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        if(hasActiveObservers()){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, LocationRequest(), this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.i(TAG, "onConnectionSuspended")
    }
    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i(TAG, "onConnectionFailed")
    }

    fun changeLocation(){
        if(googleApiClient.isConnected) this.onConnected(null)
    }
}