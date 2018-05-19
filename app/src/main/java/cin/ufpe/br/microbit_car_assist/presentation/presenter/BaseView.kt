package cin.ufpe.br.microbit_car_assist.presentation.presenter

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/18/2018 9:28 PM
 */

interface BaseView {
    fun showLoading()
    fun hideLoading()
    fun showError(error: String)
}