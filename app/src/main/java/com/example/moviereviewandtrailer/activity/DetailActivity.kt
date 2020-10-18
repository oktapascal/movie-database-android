package com.example.moviereviewandtrailer.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.moviereviewandtrailer.R
import com.example.moviereviewandtrailer.moviemodel.Constant
import com.example.moviereviewandtrailer.moviemodel.DetailResponse
import com.example.moviereviewandtrailer.retrofit.ApiServices
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private  val TAG: String = "DetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupListener()
        setupView()
    }

    override fun onStart() {
        super.onStart()
        getMoviesDetail()
    }

    private fun setupView() {
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupListener() {
        fab_play.setOnClickListener{
            startActivity(Intent(applicationContext, TrailerActivity::class.java))
        }
    }

    private fun getMoviesDetail() {
        ApiServices().end_point.getMovieDetail(Constant.MOVIE_ID, Constant.api_key)
            .enqueue(object: Callback<DetailResponse>{
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if(response.isSuccessful) {
                        showMovie(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.d(TAG, t.toString())
                }

            })
    }

    fun showMovie(detail:DetailResponse) {
        val backdrop_path = Constant.BACKDROP_PATH + detail.backdrop_path
        Picasso.get().load(backdrop_path)
            .placeholder(R.drawable.placeholder_portrait)
            .error(R.drawable.placeholder_portrait)
            .fit().centerCrop()
            .into(image_poster);

        text_title.text = detail.title
        text_votes.text = detail.vote_average.toString()
        text_overview.text = detail.overview

        for(genre in detail.genres!!) {
            text_genres.text = "${genre.name} "
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}