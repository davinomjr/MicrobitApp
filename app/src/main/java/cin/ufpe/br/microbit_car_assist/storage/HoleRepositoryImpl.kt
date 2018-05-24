package cin.ufpe.br.microbit_car_assist.storage

import cin.ufpe.br.microbit_car_assist.domain.entities.Hole
import io.reactivex.Single
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

    override fun getHoles(): List<Hole> {
        return Collections.singletonList(Hole(10.0, 10.0, "t"))
    }

//
//    override fun getHoles(callback: (holes: List<Hole?>) -> Unit) {
//        Database.it.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onCancelled(error: DatabaseError?) {
//                println(error)
//            }
//
//            override fun onDataChange(dataSnapshot: DataSnapshot?) {
//                val holes: List<Hole?>  = dataSnapshot!!.children.map { it.getValue<Hole>(Hole::class.java)}
//                callback(holes)
//            }
//        })
//    }

}