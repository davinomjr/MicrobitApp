package cin.ufpe.br.microbit_car_assist.di.modules

import android.app.Application
import android.content.Context
import cin.ufpe.br.microbit_car_assist.App
import cin.ufpe.br.microbit_car_assist.storage.HoleRepository
import cin.ufpe.br.microbit_car_assist.storage.HoleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/22/2018 6:59 PM
 */

@Module
class AppModule (val application: App) {

    @Provides @Singleton fun provideApp(): App = application
}