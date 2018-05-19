package cin.ufpe.br.microbit_car_assist.presentation.presenter

import android.view.View
import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import cin.ufpe.br.microbit_car_assist.domain.executor.Executor
import cin.ufpe.br.microbit_car_assist.domain.executor.MainThread
import cin.ufpe.br.microbit_car_assist.domain.interactor.HoleInteractor
import cin.ufpe.br.microbit_car_assist.domain.interactor.HoleInteractorImpl
import cin.ufpe.br.microbit_car_assist.storage.HoleRepositoryImpl

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/18/2018 9:27 PM
 */

class HolePresenterImpl (mExecutor: Executor, mMainThread: MainThread, val mView: HolePresenter.View) :
        AbsPresenter(mExecutor, mMainThread),
        HolePresenter,
        HoleInteractor.Callback {

    override fun resume() {
        val interactor: HoleInteractor = HoleInteractorImpl(mExecutor,mMainThread,this, HoleRepositoryImpl())
        interactor.execute()
    }

    override fun pause() {

    }

    override fun stop() {

    }

    override fun destroy() {

    }

    override fun onError(msg: String) {
        mView.hideLoading()
        mView.showError(msg)
    }

    override fun onHolesLoaded(holes: List<Hole>) {
        mView.showHolesOnMap(holes)
        mView.showLoading()
    }

}