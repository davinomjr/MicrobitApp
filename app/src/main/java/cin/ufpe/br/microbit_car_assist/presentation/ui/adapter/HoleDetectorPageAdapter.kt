package cin.ufpe.br.microbit_car_assist.presentation.ui.adapter

import android.content.Context
import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.presentation.ui.fragment.HoleDetectingFragment
import cin.ufpe.br.microbit_car_assist.presentation.ui.fragment.HoleMapFragment

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 12:41 PM
 */

class HoleDetectorPageAdapter(fm: FragmentManager, val context: Context) : FragmentPagerAdapter(fm)  {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> { HoleDetectingFragment() }
            1 -> { HoleMapFragment() }
            else -> { TODO("No other fragment to return ") }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> context.getString(R.string.detecting_title)
            1 -> context.getString(R.string.hole_map_title)
            else -> { TODO("No other title") }
        }
    }
}