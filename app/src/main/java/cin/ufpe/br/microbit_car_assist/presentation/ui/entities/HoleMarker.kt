package cin.ufpe.br.microbit_car_assist.presentation.ui.entities

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/28/2018 6:46 PM
 */

class HoleMarker(private val lat: Double, private val long: Double, private val title: String, private val snippet: String = "") : ClusterItem {

    private var position: LatLng = LatLng(lat,long)

    override fun getTitle() = title
    override fun getSnippet() = snippet
    override fun getPosition() = this.position
}