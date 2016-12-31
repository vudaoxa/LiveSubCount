package com.icomhealthtap.icom.icomhealthtap.utils.network.services

import com.google.gson.GsonBuilder
import net.lc.utils.network.adapters.RxErrorHandlingCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by mrvu on 06/12/2016.
 */
object RxServiceFactory {
    fun <T> createRetrofitService(clazz: Class<T>, endPoint: String):T{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .baseUrl(endPoint)
                .client(httpClient)
                .build()
        return retrofit.create(clazz)
    }
}