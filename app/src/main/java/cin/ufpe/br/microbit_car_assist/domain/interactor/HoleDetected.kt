package cin.ufpe.br.microbit_car_assist.domain.interactor

import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import cin.ufpe.br.microbit_car_assist.storage.HoleRepository
import com.davinomjr.base.domain.Either
import com.davinomjr.base.domain.Failure
import com.davinomjr.base.interactor.UseCase
import javax.inject.Inject
import com.davinomjr.base.interactor.UseCase.None as None

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 1:46 AM
 */

class HoleDetected
@Inject constructor(private val holeRepository: HoleRepository) : UseCase<Hole, Hole>()
{
    override suspend fun run(hole: Hole): Either<Failure, Hole> {
        return try{
            holeRepository.addHole(hole)
            Either.Right(hole)
        }
        catch (ex: Exception){
            Either.Left(Failure.ServerError(ex.localizedMessage))
        }
    }
}