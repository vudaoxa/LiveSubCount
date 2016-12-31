package net.lc.utils.network

import com.icomhealthtap.icom.icomhealthtap.utils.network.services.RxServiceFactory
import net.lc.utils.NetConstants

/**
 * Created by HP on 11/26/2016.
 */

class RxRetrofitService {
    val rxApiServices: RxApiServices
    init {
        rxApiServices = RxServiceFactory.createRetrofitService(RxApiServices::class.java, NetConstants.BASE_URL)
    }
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
