package cin.ufpe.br.microbit_car_assist.presentation.presenter

import cin.ufpe.br.microbit_car_assist.domain.executor.MainThread
import cin.ufpe.br.microbit_car_assist.domain.executor.Executor

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/18/2018 9:09 PM
 */

abstract class AbsPresenter (protected val mExecutor: Executor, protected val mMainThread: MainThread)