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

class HoleDetector @Inject constructor() : UseCase<HoleDetector.HoleDetectorResult, AccelerometerData>() {

    private val ACCEL_Z_THRESHOLD = 0.42

    override suspend fun run(data: AccelerometerData): Either<Failure, HoleDetector.HoleDetectorResult> {
        val detectorResult = if(data.accel_z <= ACCEL_Z_THRESHOLD){
            HoleDetectorResult(true, data)
        }
        else{
            HoleDetectorResult(false, data)
        }

        return Either.Right(detectorResult)
    }

    data class HoleDetectorResult(val isHole: Boolean = false, val data: AccelerometerData)
}