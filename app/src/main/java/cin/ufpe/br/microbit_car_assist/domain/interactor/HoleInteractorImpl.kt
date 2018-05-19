package cin.ufpe.br.microbit_car_assist.domain.interactor

import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import cin.ufpe.br.microbit_car_assist.domain.executor.Executor
import cin.ufpe.br.microbit_car_assist.domain.executor.MainThread
import cin.ufpe.br.microbit_car_assist.storage.HoleRepository

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/18/2018 11:41 AM
 */

class HoleInteractorImpl(mExecutor: Executor,
                         mMainThread: MainThread,
                         val mCallback: HoleInteractor.Callback,
                         val mRepository: HoleRepository)
    : AbsInteractor(mExecutor, mMainThread), HoleInteractor {


    override fun run() {
        // Get Holes
        var holes: List<Hole> = mRepository.getHoles()

//        if(holes.isEmpty()){
//            mMainThread.post(Runnable { mCallback.onError() })
//            return
//        }

        mMainThread.post(Runnable { mCallback.onHolesLoaded(holes) })
    }
}