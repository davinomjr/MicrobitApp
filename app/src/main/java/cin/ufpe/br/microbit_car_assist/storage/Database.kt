package cin.ufpe.br.microbit_car_assist.storage

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/04/2018 3:20 PM
 */

class Database {

    companion object {

        init{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }

        val it: DatabaseReference = FirebaseDatabase.getInstance().getReference("holes")

    }

}