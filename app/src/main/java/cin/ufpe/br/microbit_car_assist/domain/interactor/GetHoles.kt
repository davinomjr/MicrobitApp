package cin.ufpe.br.microbit_car_assist.domain.interactor

import android.util.Log
import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleView
import cin.ufpe.br.microbit_car_assist.storage.HoleRepository
import com.davinomjr.base.domain.Either
import com.davinomjr.base.domain.Failure
import com.davinomjr.base.interactor.UseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/17/2018 9:30 PM
 */

class GetHoles
@Inject constructor(private val holeRepository: HoleRepository) : UseCase<UseCase.None, (holes: List<Hole?>) -> Unit>() {

    private val TAG = GetHoles::class.java.simpleName

    override suspend fun run(callback: (holes: List<Hole?>) -> Unit): Either<Failure, None> {
        Log.i(TAG , "run")
        holeRepository.getHoles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { dataSnapshot ->
                    val holes = dataSnapshot!!.children.map { it.getValue<Hole>(Hole::class.java) }
                    callback(holes)
                })

        return Either.Right(None())
    }
}