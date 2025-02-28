package com.codepath.articlesearch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.articlesearch.databinding.ActivityMainBinding
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val TAG = "MainActivity/"
var SEARCH_API_KEY = BuildConfig.API_KEY
var SHOW_SEARCH_URL =
    "https://api.themoviedb.org/3/tv/popular?&api_key=${SEARCH_API_KEY}"

class MainActivity : AppCompatActivity() {
    private val shows = mutableListOf<TVShow>()

    private lateinit var showsRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        showsRecyclerView = findViewById(R.id.shows)
        // TODO: Set up ArticleAdapter with shows
        val showAdapter = ShowAdapter(this, shows)
        showsRecyclerView.adapter = showAdapter

        showsRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            showsRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        val client = AsyncHttpClient()
        client.get(SHOW_SEARCH_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch shows: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched shows: $json")
                try {
                    // TODO: Create the parsedJSON
                    val parsedJson = createJson().decodeFromString(
                        TVShowsResponse.serializer(),
                        json.jsonObject.toString()
                    )
                    // TODO: Do something with the returned json (contains show information)
                    parsedJson.results?.let { list ->
                        shows.addAll(list)
                    }
                    // TODO: Save the shows and reload the screen
                    parsedJson.results.let { list ->
                        if (list != null) {
                            shows.addAll(list)
                        }

                        // Reload the screen
                        showAdapter.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }

        })

    }
}