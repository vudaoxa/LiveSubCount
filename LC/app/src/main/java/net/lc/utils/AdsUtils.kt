package net.lc.utils

import android.content.Context
import com.google.android.gms.ads.*
import com.tieudieu.util.DebugLog
import net.live.sub.R
import java.util.*


/**
 * Created by tusi on 2/17/17.
 */
fun initAds(context: Context) {
    context.apply {
        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id))
        MobileAds.initialize(this, getString(R.string.inter_ad_unit_id))
    }
    initMapAdsErrors()

}

var time = -1L
val DURATION = 60000
val adRequest = AdRequest.Builder().build()
fun loadBannerAds(mAdView: AdView) {
    val currentTime = System.currentTimeMillis()
    if (currentTime - time >= DURATION) {
        time = currentTime
    } else return
    DebugLog.e("loadBannerAds----------------")
    mAdView.loadAd(adRequest)
    mAdView.adListener = adBannerListener
}

val mapAdsErrors = HashMap<Int, String>()
fun initMapAdsErrors() {
    mapAdsErrors.put(AdRequest.ERROR_CODE_INTERNAL_ERROR, "ERROR_CODE_INTERNAL_ERROR")
    mapAdsErrors.put(AdRequest.ERROR_CODE_INVALID_REQUEST, "ERROR_CODE_INVALID_REQUEST")
    mapAdsErrors.put(AdRequest.ERROR_CODE_NETWORK_ERROR, "ERROR_CODE_NETWORK_ERROR")
    mapAdsErrors.put(AdRequest.ERROR_CODE_NO_FILL, "ERROR_CODE_NO_FILL")
}

//var mInterstitialAd: InterstitialAd? = null
fun initInterAds(context: Context, adListener: AdListener): InterstitialAd? {
    var mInterstitialAd = InterstitialAd(context)
    mInterstitialAd?.apply {
        adUnitId = context.getString(R.string.inter_ad_unit_id)
        this.adListener = adListener
    }
    requestNewInterstitial(mInterstitialAd)
    return mInterstitialAd
}

fun requestNewInterstitial(mInterstitialAd: InterstitialAd?) {
    val adRequest = AdRequest.Builder().build()
    mInterstitialAd?.loadAd(adRequest)
}

fun showInterAds(mInterstitialAd: InterstitialAd?): Boolean {
    mInterstitialAd?.apply {
        if (isLoaded) {
            show()
            return true
        } else return false
    }
    return false
}

val adBannerListener = object : AdListener() {
    override fun onAdClosed() {
        sendHit(CATEGORY_ACTION, ACTION_onAdClosed)
    }

    override fun onAdFailedToLoad(p0: Int) {
        sendHit(CATEGORY_ACTION, mapAdsErrors.get(p0) as String)
    }

    override fun onAdOpened() {
        sendHit(CATEGORY_ACTION, ACTION_onAdOpened)
    }

    override fun onAdLoaded() {
        sendHit(CATEGORY_ACTION, ACTION_onAdLoaded)
    }

    override fun onAdLeftApplication() {
        sendHit(CATEGORY_ACTION, ACTION_onAdLeftApplication)
    }
}