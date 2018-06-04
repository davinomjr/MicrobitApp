package cin.ufpe.br.microbit_car_assist.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import cin.ufpe.br.microbit_car_assist.domain.interactor.GetHoles
import cin.ufpe.br.microbit_car_assist.domain.interactor.HoleDetected
import com.davinomjr.base.interactor.UseCase
import com.davinomjr.base.viewmodel.BaseViewModel
import kotlinx.coroutines.experimental.handleCoroutineException
import javax.inject.Inject
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 12:03 PM
 */

class HolesViewModel
@Inject constructor(private val getHoles: GetHoles) : BaseViewModel() {

    val holes: MutableLiveData<List<HoleView>> = MutableLiveData()

    fun getHoles(): Disposable? {
        return getHoles.run()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    holes.postValue(convertToHoleViewList(it))
                })
    }

    fun convertToHoleViewList(holes: List<Hole?>) : List<HoleView>{
        return holes.map{ hole -> HoleView(hole!!.latitude,hole!!.longitude,hole!!.date)}
    }
}