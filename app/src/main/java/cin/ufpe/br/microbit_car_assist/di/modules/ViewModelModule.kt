package cin.ufpe.br.microbit_car_assist.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import cin.ufpe.br.microbit_car_assist.di.ViewModelFactory
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleDetectorViewModel
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HolesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 1:53 AM
 */

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HolesViewModel::class)
    abstract fun bindHolesViewModel(holesViewModel: HolesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HoleDetectorViewModel::class)
    abstract fun bindHoleDetectorViewModel(holeDetectorViewModel: HoleDetectorViewModel): ViewModel
}