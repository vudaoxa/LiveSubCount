package net.lc.utils.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.icomhealthtap.icom.icomhealthtap.utils.network.services.RxServiceFactory
import com.tieudieu.util.DebugLog
import net.lc.utils.Constants

import net.lc.utils.Models
import net.lc.utils.NetConstants

import java.io.IOException
import java.util.concurrent.TimeUnit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by HP on 11/26/2016.
 */

class RetrofitService {
    val service: ApiServices
    init {
        service = RxServiceFactory.createRetrofitService(ApiServices::class.java, NetConstants.BASE_URL)
    }
    companion object {
        private var mInstance: RetrofitService? = null
        val instance: RetrofitService
            get() {
                if (mInstance == null)
                    mInstance = RetrofitService()
                return mInstance!!
            }
    }
}
