package cin.ufpe.br.microbit_car_assist.di.modules

import cin.ufpe.br.microbit_car_assist.storage.HoleRepository
import cin.ufpe.br.microbit_car_assist.storage.HoleRepositoryImpl
import dagger.Binds
import dagger.Module

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 4:40 PM
 */

@Module
abstract class AbsAppModule {
    @Binds
    abstract fun holeRepository(holeRepositoryImpl: HoleRepositoryImpl) : HoleRepository
}