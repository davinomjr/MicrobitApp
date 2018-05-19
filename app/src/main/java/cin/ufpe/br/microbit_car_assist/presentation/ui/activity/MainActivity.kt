package cin.ufpe.br.microbit_car_assist.presentation.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.presentation.ui.adapter.PageAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Microbit Car Assist"

        viewpager_main.adapter = PageAdapter(supportFragmentManager)
        tabs_main.setupWithViewPager(viewpager_main)
    }

}