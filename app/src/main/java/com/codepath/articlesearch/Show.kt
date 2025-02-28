package com.codepath.articlesearch
import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TVShowsResponse(
    @SerialName("results")
    val results: List<TVShow>?
)

@Keep
@Serializable
data class TVShow(
    @SerialName("name")
    val showname: String?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("overview")
    val overview: String?,
    @SerialName("vote_average")
    val voteAverage: Double?,
    @SerialName("first_air_date")
    val firstAirDate: String?
) : java.io.Serializable {

    // Construct the full image URL
    val mediaImageUrl: String?
        get() = "https://image.tmdb.org/t/p/w500$posterPath"
}

