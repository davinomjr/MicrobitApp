package com.davinomjr.base.ui

import android.arch.lifecycle.LifecycleObserver
import android.support.v4.app.Fragment
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import cin.ufpe.br.microbit_car_assist.App
import cin.ufpe.br.microbit_car_assist.R
import cin.ufpe.br.microbit_car_assist.di.AppComponent
import javax.inject.Inject

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 12:43 PM
 */

abstract class BaseFragment : Fragment(), LifecycleObserver {

    val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as App).component
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}