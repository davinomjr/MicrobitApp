package cin.ufpe.br.microbit_car_assist.domain.executor

import cin.ufpe.br.microbit_car_assist.domain.interactor.AbsInteractor
import cin.ufpe.br.microbit_car_assist.domain.interactor.Interactor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS



/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/17/2018 7:10 PM
 */

class ThreadExecutor : Executor {


    // This is a singleton
    // DAVINO: TODO("CHANGE THIS LATER!!)
    @Volatile
    private var sThreadExecutor: ThreadExecutor? = null

    private val CORE_POOL_SIZE = 3
    private val MAX_POOL_SIZE = 5
    private val KEEP_ALIVE_TIME = 120
    private val TIME_UNIT = TimeUnit.SECONDS
    private val WORK_QUEUE = LinkedBlockingQueue<Runnable>()

    private lateinit var mThreadPoolExecutor: ThreadPoolExecutor

    init{
        val keepAlive = KEEP_ALIVE_TIME.toLong()
        mThreadPoolExecutor = ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                keepAlive,
                TIME_UNIT,
                WORK_QUEUE)
    }


    override fun execute(interactor: Interactor) {
        mThreadPoolExecutor.submit {
            interactor.execute()
            interactor.onFinished()
        }
    }

    /**
     * Returns a singleton instance of this executor. If the executor is not initialized then it initializes it and returns
     * the instance.
     */
    fun getInstance(): ThreadExecutor? {
        if (sThreadExecutor == null) {
            sThreadExecutor = ThreadExecutor()
        }

        return sThreadExecutor
    }
}