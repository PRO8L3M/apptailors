package com.apptailors.streams.data.repository

import com.apptailors.streams.R
import com.apptailors.streams.data.entity.StreamOverview
import com.apptailors.streams.data.network.ApptailorsApi
import com.apptailors.streams.data.network.safeCall
import com.apptailors.streams.ext.bodyOrException

class Repository(private val api: ApptailorsApi) {

    suspend fun getQuestion(id: Int) = safeCall { api.getQuestion(id).bodyOrException() }

    /**
     * Mocked list of streams
     * */
    fun getStreamList() = listOf(
        StreamOverview(1,"Manchester United vs Manchester City", "Premiere League", "live now", R.drawable.ic_sky_sports_logo, "", R.drawable.manchester),
        StreamOverview(2,"L.A. Lakers vs L.A. Clippers", "Playoffs", "tomorrow", R.drawable.espn, "Tuesday, 12:30PM", R.drawable.nba),
        StreamOverview(3,"Mayweather vs McGregor", "Lightweight Championship", "finished", R.drawable.ic_sky_sports_logo,"" , R.drawable.boxing)
    )
}