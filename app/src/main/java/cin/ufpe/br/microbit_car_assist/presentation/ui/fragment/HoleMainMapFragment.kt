package cin.ufpe.br.microbit_car_assist.presentation.ui.fragment


import android.os.Bundle
import android.util.Log


/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 */
class HoleMainMapFragment : BaseHoleMapFragment() {

    private val TAG: String = HoleMainMapFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }
}