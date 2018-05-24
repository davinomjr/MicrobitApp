package com.davinomjr.extension


import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.davinomjr.base.ui.BaseFragment

/**
 * Created by Davino Junior - dmtsj@{cin.ufpe.br, gmail.com}
 * at 05/23/2018 3:02 PM
 */

inline fun <reified T : ViewModel> BaseFragment.viewModel(body: T.() -> Unit = {}): T {
    val vm = ViewModelProviders.of(this, this.viewModelFactory)[T::class.java]
    vm.body()
    return vm
}