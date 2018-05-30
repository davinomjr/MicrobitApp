package cin.ufpe.br.microbit_car_assist.presentation.ui.util

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import org.jetbrains.anko.alert
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton


/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/08/2018 11:42 PM
 */

class MessageUtil {

    companion object {
        fun showMessage(context: Context, message: String, length: Int = Toast.LENGTH_LONG) = context.toast(message)
        fun showSnack(view: View, message: String) = snackbar(view, message)


        fun showAlert(context: Context, message: String){
            context.alert(message).show()
        }
        fun showAlert(context: Context, message: String, yesCallback: (dialog: DialogInterface) -> Unit){
            context.alert(message) {
                yesButton(yesCallback)
            }.show()
        }

        fun showAlert(context: Context, message: String, yesCallback: (dialog: DialogInterface) -> Unit, noCallback: (dialog: DialogInterface) -> Unit){
            context.alert(message) {
                yesButton(yesCallback)
                noButton(noCallback)
            }.show()
        }
    }
}