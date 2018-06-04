package cin.ufpe.br.microbit_car_assist.presentation.ui.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.presentation.data.LocationLiveData
import cin.ufpe.br.microbit_car_assist.presentation.ui.adapter.MainPageAdapter
import cin.ufpe.br.microbit_car_assist.presentation.ui.fragment.HoleMapFragment
import cin.ufpe.br.microbit_car_assist.presentation.ui.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.design.snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Microbit Car Assist"

        viewpager_main.adapter = MainPageAdapter(supportFragmentManager, this)
        tabs_main.setupWithViewPager(viewpager_main)
    }

}
