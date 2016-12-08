package net.lc.utils.network

import com.google.gson.GsonBuilder
import com.tieudieu.util.DebugLog
import net.lc.utils.Constants
import net.lc.utils.Models
import net.lc.utils.NetConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by HP on 11/26/2016.
 */

class RetrofitService {
    private val service: ApiServices

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                //                .addInterceptor(new InterceptorRefreshToken())
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

        val retrofit = Retrofit.Builder()
//                .addCallAdapterFactory()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(NetConstants.BASE_URL)
                .client(httpClient)
                .build()
        service = retrofit.create(ApiServices::class.java)

    }


    fun refeshToken() {
        synchronized(RetrofitService::class.java) {
            mInstance = RetrofitService()
        }

    }

    fun getChannelInfo(channelName:String): Observable<Models.ChannelListResponse> {
        return Observable.create {
            subscriber ->
            val callResponse = service.getChannelInfo(Constants.API_KEY, Constants.PART_STATISTICS, channelName)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                val dataResponse = response.body()
                DebugLog.e("dataResponse-------------- "+dataResponse)
                subscriber.onNext(dataResponse)
                subscriber.onCompleted()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
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
