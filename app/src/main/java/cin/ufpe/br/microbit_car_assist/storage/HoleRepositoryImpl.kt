package cin.ufpe.br.microbit_car_assist.storage

import android.util.Log
import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import com.google.firebase.database.*
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Cancellable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher
import java.util.*
import javax.inject.Inject

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/17/2018 9:40 PM
 */

class HoleRepositoryImpl @Inject constructor() : HoleRepository{

    private val TAG = HoleRepositoryImpl::class.java.simpleName

    override fun addHole(hole: Hole) {
        val id: String = hole.id
        FirebaseDatabase.getInstance().getReference("holes").child(id).setValue(hole)
    }

    override fun getHoles(): Observable<DataSnapshot> {
        Log.i(TAG, "getHoles()")
        return Observable.create({

            val singleValueListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    println(error)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.i(TAG, "got ${dataSnapshot.children.map { it.getValue<Hole>(Hole::class.java)!!}.count()} Holes")
                    val holes = dataSnapshot.children.map { it.getValue<Hole>(Hole::class.java)!!}
                    for(hole in holes){
                        Log.i(TAG, "got hole with lat = ${hole.latitude} and long = ${hole.longitude}")
                    }

                    FirebaseDatabase.getInstance().getReference("holes").removeEventListener(this)
                    Log.i(TAG, "removing listener")
                    it.onNext(dataSnapshot)
                }
            }

            it.setCancellable({ Database.it.removeEventListener(singleValueListener) })
            FirebaseDatabase.getInstance().getReference("holes").addListenerForSingleValueEvent(singleValueListener)
            Log.i(TAG, "adding listener")
        })
    }
}