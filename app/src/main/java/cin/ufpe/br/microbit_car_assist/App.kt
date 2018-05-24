package cin.ufpe.br.microbit_car_assist

import android.app.Application
import android.util.Log
import cin.ufpe.br.microbit_car_assist.di.AppComponent
import cin.ufpe.br.microbit_car_assist.di.DaggerAppComponent
import cin.ufpe.br.microbit_car_assist.di.modules.AppModule

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/22/2018 4:06 PM
 */

class App: Application() {

    val component: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("App","onCreate()")
        component.inject(this)
    }

}