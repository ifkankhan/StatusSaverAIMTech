package com.example.statussaveraim.utils

import androidx.lifecycle.MutableLiveData

object Constants {

    var IsAppOnTimer = false
    var billingWeekly = MutableLiveData<String>("Rs 410")
    var billingMonthly = MutableLiveData<String>("Rs 820")
    var billingYearly = MutableLiveData<String>("Rs 2,200")
}