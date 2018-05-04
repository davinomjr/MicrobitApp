package cin.ufpe.br.microbit_car_assist.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/04/2018 3:20 PM
 */

class Database {

    companion object {
        fun it() : DatabaseReference {
            return FirebaseDatabase.getInstance().getReference("holes")
        }

        fun configure(){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }
    }

}