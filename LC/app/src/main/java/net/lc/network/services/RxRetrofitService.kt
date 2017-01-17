package net.lc.network

import com.icomhealthtap.icom.icomhealthtap.utils.network.services.RxServiceFactory
import net.lc.utils.NetConstants

/**
 * Created by HP on 11/26/2016.
 */

class RxRetrofitService {
    val rxApiServices = RxServiceFactory.createRetrofitService(RxApiServices::class.java, NetConstants.BASE_URL)
    val rxxApiServices = RxServiceFactory.createRetrofitService(RxxApiServices::class.java, NetConstants.BASE_URL_LC)
    companion object {
        private var mInstance: RxRetrofitService? = null
        val instance: RxRetrofitService
            get() {
                if (mInstance == null)
                    mInstance = RxRetrofitService()
                return mInstance!!
            }
    }
}
