package cin.ufpe.br.microbit_car_assist.domain.interactor

import cin.ufpe.br.microbit_car_assist.domain.executor.Executor
import cin.ufpe.br.microbit_car_assist.domain.executor.MainThread

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/18/2018 11:54 AM
 */

abstract class AbsInteractor(val mExecutor: Executor, var mMainThread: MainThread) : Interactor {

    var mIsCanceled = false
    var mIsRunning = false

    abstract fun run()

    fun cancel(){
        mIsCanceled = true
        mIsRunning = false
    }

    override fun onFinished(){
        mIsRunning = false
        mIsCanceled = false
    }

    override fun execute(){
        mIsRunning = true
        mExecutor.execute(this)
    }
}