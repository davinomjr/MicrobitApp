package cin.ufpe.br.microbit_car_assist.di

import cin.ufpe.br.microbit_car_assist.App
import cin.ufpe.br.microbit_car_assist.di.modules.AbsAppModule
import cin.ufpe.br.microbit_car_assist.di.modules.AppModule
import cin.ufpe.br.microbit_car_assist.di.modules.ViewModelModule
import cin.ufpe.br.microbit_car_assist.domain.interactor.HoleDetected
import cin.ufpe.br.microbit_car_assist.presentation.lifecycle.AccelerometerBluetoothObserver
import cin.ufpe.br.microbit_car_assist.presentation.ui.activity.BaseActivity
import cin.ufpe.br.microbit_car_assist.presentation.ui.activity.HoleDetectorActivity
import cin.ufpe.br.microbit_car_assist.presentation.ui.fragment.BaseHoleMapFragment
import cin.ufpe.br.microbit_car_assist.presentation.ui.fragment.HoleDetectingFragment
import cin.ufpe.br.microbit_car_assist.presentation.ui.fragment.HoleMainMapFragment
import cin.ufpe.br.microbit_car_assist.presentation.ui.fragment.HoleMapFragment
import cin.ufpe.br.microbit_car_assist.storage.HoleRepository
import cin.ufpe.br.microbit_car_assist.storage.HoleRepositoryImpl
import com.davinomjr.base.ui.BaseFragment
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/22/2018 6:59 PM
 */

@Singleton
@Component(modules = [
        AppModule::class,
        AbsAppModule::class,
        ViewModelModule::class
])
interface AppComponent {
    fun inject(app: App)
    fun inject(mapFragment: BaseHoleMapFragment)
    fun inject(holeDetectorFragment: HoleDetectingFragment)
}