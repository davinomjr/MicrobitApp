package cin.ufpe.br.microbit_car_assist.presentation.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.presentation.ui.adapter.HoleDetectorPageAdapter
import cin.ufpe.br.microbit_car_assist.presentation.ui.fragment.HoleDetectingFragment
import cin.ufpe.br.microbit_car_assist.presentation.ui.fragment.HoleMapFragment
import cin.ufpe.br.microbit_car_assist.presentation.ui.fragment.MainFragment
import cin.ufpe.br.microbit_car_assist.presentation.viewmodel.HoleDetectorViewModel
import kotlinx.android.synthetic.main.activity_main.*
import com.bluetooth.mwoolley.microbitbledemo.*
import com.davinomjr.extension.viewModel

class HoleDetectorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hole_detector)

        viewpager_main.adapter = HoleDetectorPageAdapter(supportFragmentManager, this)
        tabs_main.setupWithViewPager(viewpager_main)
    }
}
