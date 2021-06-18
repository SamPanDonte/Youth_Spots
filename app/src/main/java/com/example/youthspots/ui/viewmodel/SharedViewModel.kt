package com.example.youthspots.ui.viewmodel

import android.content.Context
import com.example.youthspots.data.Repository
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class SharedViewModel : BaseViewModel() {
    var interstitialAd: InterstitialAd? = null
    var adsLoaded = false
    private var adCounter: Long = Repository.fromSP(Repository.AD_COUNTER_TAG, 4)
        get () {
            return if (field == 0L) {
                field = 4
                0
            } else {
                field -= 1
                field
            }
        }

    fun loadAd(context: Context) {
        if (interstitialAd == null) {
            InterstitialAd.load(
                context, "ca-app-pub-3940256099942544/1033173712",
                AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) { interstitialAd = ad }
                }
            )
        }
    }

    fun getCounterAd() = if (adCounter == 0L) {
        val ad = interstitialAd
        interstitialAd = null
        ad
    } else { null }

    fun getAd(): InterstitialAd? {
        val ad = interstitialAd
        interstitialAd = null
        return ad
    }

    fun save() {
        Repository.saveSP(Repository.AD_COUNTER_TAG, adCounter)
    }
}