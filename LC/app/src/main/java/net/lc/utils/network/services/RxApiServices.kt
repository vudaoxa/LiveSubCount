package net.lc.utils.network

//import com.google.api.services.youtube.model.ChannelListResponse

import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.Observable
import net.lc.utils.Models
import net.lc.utils.NetConstants
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by HP on 11/25/2016.
 */

interface RxApiServices {

    @GET(NetConstants.API_YOUTUBE_CHANNEL)
    fun requestChannelInfo(@Query("key") developerKey: String, @Query("part") part: String,
                           @Query("forUsername") forUsername: String): Observable<Models.ChannelListResponse>

    @GET(NetConstants.API_YOUTUBE_SEARCH)
    fun requestSearch(@Query("key") developerKey: String, @Query("part") part: String, @Query("pageToken") pageToken: String?,
                      @Query("q") q: String, @Query("type") type: String): Observable<SearchListResponse>
}
