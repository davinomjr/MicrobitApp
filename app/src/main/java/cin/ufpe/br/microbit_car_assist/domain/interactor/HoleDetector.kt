package cin.ufpe.br.microbit_car_assist.domain.interactor

import cin.ufpe.br.microbit_car_assist.domain.entities.AccelerometerData
import com.davinomjr.base.domain.Either
import com.davinomjr.base.domain.Failure
import com.davinomjr.base.interactor.UseCase
import javax.inject.Inject

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/24/2018 7:19 PM
 */

class HoleDetector
@Inject constructor()
    : UseCase<HoleDetector.HoleDetectorResult, AccelerometerData>() {

    override suspend fun run(params: AccelerometerData): Either<Failure, HoleDetector.HoleDetectorResult> {
        // Algorithm to detect if it is a role
        val result = HoleDetectorResult(true, params)
        return Either.Right(result)
    }

    data class HoleDetectorResult(val isHole: Boolean = false, val data: AccelerometerData)
}