package cin.ufpe.br.microbit_car_assist.storage

import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
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

    override fun addHole(hole: Hole) {
        val id: String = hole.id
        Database.it.child(id).setValue(hole)
    }


    fun observeValue() : Flowable<DataSnapshot> {
        return Flowable.create(FlowableOnSubscribe<DataSnapshot>(){
            fun subscribe(emitter: FlowableEmitter<DataSnapshot>){
                val valueEventListener = object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        if(!emitter.isCancelled){
                            emitter.onError(Exception(error.message))
                        }
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        emitter.onNext(snapshot)
                    }
                }

                emitter.setCancellable(Cancellable {
                    fun cancel(){
                        Database.it.removeEventListener(valueEventListener)
                    }
                })

                Database.it.addValueEventListener(valueEventListener)
            }
        }, BackpressureStrategy.MISSING)
    }


     override fun getHoles(): Observable<DataSnapshot> {
        return Observable.create({
            fun subscribe(emitter: ObservableEmitter<DataSnapshot>) {
                val singleValueListener = object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error)
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        emitter.onNext(dataSnapshot)
                    }
                }

                emitter.setCancellable({ Database.it.removeEventListener(singleValueListener) })

                Database.it.addListenerForSingleValueEvent(singleValueListener)
            }
        })
    }

//
//            Database.it.addListenerForSingleValueEvent(object: ValueEventListener {
//                override fun onCancelled(error: DatabaseError?) {
//                    println(error)
//                }
//
//                override fun onDataChange(dataSnapshot: DataSnapshot?) {
//                    val holes: List<Hole?>  = dataSnapshot!!.children.map { it.getValue<Hole>(Hole::class.java)}
//                    callback(holes)
//                }
//            })

    }