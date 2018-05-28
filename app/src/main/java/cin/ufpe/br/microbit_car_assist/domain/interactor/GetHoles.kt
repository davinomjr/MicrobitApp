package cin.ufpe.br.microbit_car_assist.domain.interactor

import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleView
import cin.ufpe.br.microbit_car_assist.storage.HoleRepository
import com.davinomjr.base.domain.Either
import com.davinomjr.base.domain.Failure
import com.davinomjr.base.interactor.UseCase
import javax.inject.Inject

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/17/2018 9:30 PM
 */

class GetHoles
@Inject constructor(private val holeRepository: HoleRepository) : UseCase<UseCase.None, (holes: List<Hole?>) -> Unit>() {

    override suspend fun run(callback: (holes: List<Hole?>) -> Unit): Either<Failure, None> {
        holeRepository.getHoles(callback)
        return Either.Right(None())
    }
}