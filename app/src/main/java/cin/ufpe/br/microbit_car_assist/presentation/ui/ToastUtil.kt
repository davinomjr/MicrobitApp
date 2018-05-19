package cin.ufpe.br.microbit_car_assist.presentation.ui

import android.content.Context
import android.widget.Toast

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/08/2018 11:42 PM
 */

class ToastUtil {

    companion object {
        fun showMessage(context: Context, message: String, length: Int = Toast.LENGTH_LONG){
            Toast.makeText(context, message, length).show()
        }
    }
}