package net.lc.utils.network

//import com.google.api.services.youtube.model.ChannelListResponse

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
    fun getChannelInfo(@Query("key") developerKey: String, @Query("part") id: String,
                       @Query("forUsername") forUsername: String): Observable<Models.ChannelListResponse>

}
