package cin.ufpe.br.microbit_car_assist.domain.executor

import android.os.Handler
import android.os.Looper

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/17/2018 7:08 PM
 */

class MainThread {
    val handler: Handler = Handler(Looper.getMainLooper())

    fun post(runnable: Runnable){
        handler.post(runnable)
    }
}