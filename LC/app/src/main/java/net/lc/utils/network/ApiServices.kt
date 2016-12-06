package net.lc.utils.network

//import com.google.api.services.youtube.model.ChannelListResponse
import net.lc.utils.Models
import net.lc.utils.NetConstants

import retrofit2.Call
import retrofit2.http.*
import rx.Observable


/**
 * Created by HP on 11/25/2016.
 */

interface ApiServices {
    //    @GET(NetConstants.API_YOUTUBE_CHANNEL)
//    fun getChannelInfo(@Query("key") developerKey: String, @Query("part") id: String,
//                       @Query("forUsername") forUsername: String): Observable<Models.ChannelListResponse>
//    @GET(NetConstants.API_YOUTUBE_CHANNEL)
//    fun getChannelInfo(@Query("key") developerKey: String, @Query("part") id: String,
//                       @Query("forUsername") forUsername: String): Call<Models.ChannelListResponse>
    @GET(NetConstants.API_YOUTUBE_CHANNEL)
    fun getChannelInfo(@Query("key") developerKey: String, @Query("part") id: String,
                       @Query("forUsername") forUsername: String): Observable<Models.ChannelListResponse>

}
