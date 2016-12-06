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

/* fun getChannelInfo(channelName:String): Observable<Models.ChannelListResponse> {
//        fun getChannelInfo(@Query("key") developerKey: String, @Query("part") id: String,
//                           @Query("forUsername") forUsername: String): Observable<ChannelListResponse>
//        service.getChannelInfo(Constants.API_KEY, Constants.PART_STATISTICS, channelName)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe{
//                    artifact ->
//                    println("${artifact.etag} (${artifact.items!!.get(0).statistics!!.viewCount})")
//                }

     return Observable.create {
         subscriber ->
         val callResponse = service.getChannelInfo(Constants.API_KEY, Constants.PART_STATISTICS, channelName)
         val response = callResponse.execute()

         if (response.isSuccessful) {
             val dataResponse = response.body()
             DebugLog.e("dataResponse-------------- "+dataResponse)
//                val news = dataResponse.children.map {
//                    val item = it.data
//                    RedditNewsItem(item.author, item.title, item.num_comments,
//                            item.created, item.thumbnail, item.url)
//                }
//                val redditNews = RedditNews(
//                        dataResponse.after ?: "",
//                        dataResponse.before ?: "",
//                        news)

             subscriber.onNext(dataResponse)
             subscriber.onCompleted()
         } else {
             subscriber.onError(Throwable(response.message()))
         }
     }
 }*/