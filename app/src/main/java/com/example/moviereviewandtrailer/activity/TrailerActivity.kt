package com.example.moviereviewandtrailer.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviereviewandtrailer.R
import com.example.moviereviewandtrailer.adapter.TrailerAdapter
import com.example.moviereviewandtrailer.moviemodel.Constant
import com.example.moviereviewandtrailer.moviemodel.TrailerResponse
import com.example.moviereviewandtrailer.retrofit.ApiServices
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_trailer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TrailerActivity : AppCompatActivity() {
    private  val TAG: String = "TrailerActivity"

    lateinit var trailerAdapter:TrailerAdapter
    lateinit var youTubePlayer:YouTubePlayer

    private var youTubeKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)
        setupView()
        setupReyclerView()
    }

    override fun onStart() {
        super.onStart()
        getMovieTrailer()
    }

    private fun setupView() {
        supportActionBar!!.title = Constant.MOVIE_TITLE
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                youTubeKey?.let {
                    youTubePlayer.cueVideo(it, 0f)
                }
            }
        })
    }

    private fun setupReyclerView() {
        trailerAdapter = TrailerAdapter(arrayListOf(), object : TrailerAdapter.OnAdapterListener {
            override fun OnLoad(key: String) {
                youTubeKey = key
            }

            override fun OnPlay(key: String) {
                youTubePlayer.loadVideo(key, 0f)
            }
        })
        list_video.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trailerAdapter
        }
    }

    private fun getMovieTrailer() {
        showLoading(true)
        ApiServices().end_point.getMovieTrailer(Constant.MOVIE_ID, Constant.api_key)
            .enqueue(object : Callback<TrailerResponse> {
                override fun onResponse(
                    call: Call<TrailerResponse>,
                    response: Response<TrailerResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        showTrailer(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                    showLoading(false)
                }

            })
    }

    private fun showLoading(loading: Boolean) {
        when(loading) {
            true -> {
                progress_video.visibility = View.VISIBLE
            }
            false -> {
                progress_video.visibility = View.GONE
            }
        }
    }

    private fun showTrailer(trailer: TrailerResponse) {
        trailerAdapter.setData(trailer.results)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}