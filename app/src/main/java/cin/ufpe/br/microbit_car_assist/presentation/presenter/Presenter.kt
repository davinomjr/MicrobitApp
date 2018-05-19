package cin.ufpe.br.microbit_car_assist.presentation.presenter

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/18/2018 9:11 PM
 */

interface Presenter {
    fun resume()
    fun pause()
    fun stop()
    fun destroy()
    fun onError(msg: String)
}