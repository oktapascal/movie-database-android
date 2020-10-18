package com.example.moviereviewandtrailer.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviereviewandtrailer.R
import com.example.moviereviewandtrailer.adapter.MainAdapter
import com.example.moviereviewandtrailer.moviemodel.Constant
import com.example.moviereviewandtrailer.moviemodel.MovieModel
import com.example.moviereviewandtrailer.moviemodel.MovieResponse
import com.example.moviereviewandtrailer.retrofit.ApiServices
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val moviePopular = 0
const val movieNowPlaying = 1

class MainActivity : AppCompatActivity() {

    private  val TAG: String = "MainActivity"
    private var movieCategory = 0
    private val api = ApiServices().end_point
    private var isScrolling = false
    private var currentPage = 1
    private var totalPage = 0
    lateinit var mainAdapter:MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupReyclerView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getMovie()
        showLoadingNextPage(false)
    }

    private fun setupReyclerView() {
        mainAdapter = MainAdapter(arrayListOf(), object : MainAdapter.OnAdapterListener{
            override fun OnClick(movie: MovieModel) {
                startActivity(Intent(applicationContext, DetailActivity::class.java))
            }
        })
        list_movie.apply {
            layoutManager = GridLayoutManager(context,2)
            adapter = mainAdapter
        }
    }

    private fun setupListener() {
        scroll_view.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if(scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight) {
                    if(!isScrolling) {
                        if(currentPage <= totalPage) {
                            getMovieNextPage()
                        }
                    }
                }
            }

        })
    }

    private fun getMovie() {
        scroll_view.scrollTo(0,0)
        currentPage = 1
        showLoading(true)

        var apiCall: Call<MovieResponse>? = null
        when(movieCategory) {
            moviePopular -> {
                apiCall = api.getMoviePopular(Constant.api_key, 1)
            }
            movieNowPlaying -> {
                apiCall = api.getMovieNowPlaying(Constant.api_key, 1)
            }
        }
        apiCall!!
            .enqueue(object: Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoading(false)
                    if(response.isSuccessful) {
                        showMovie(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d(TAG, "errorResponse: $t")
                    showLoading(false)
                }

            })
    }

    private fun getMovieNextPage() {
        currentPage += 1
        showLoadingNextPage(true)

        var apiCall: Call<MovieResponse>? = null
        when(movieCategory) {
            moviePopular -> {
                apiCall = api.getMoviePopular(Constant.api_key, currentPage)
            }
            movieNowPlaying -> {
                apiCall = api.getMovieNowPlaying(Constant.api_key, currentPage)
            }
        }
        apiCall!!
            .enqueue(object: Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoadingNextPage(false)
                    if(response.isSuccessful) {
                        showMovieNextPage(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d(TAG, "errorResponse: $t")
                    showLoadingNextPage(false)
                }

            })
    }

    private fun showLoading(loading:Boolean) {
        when(loading) {
            true -> progress_movie.visibility = View.VISIBLE
            false -> progress_movie.visibility = View.GONE
        }
    }

    private fun showLoadingNextPage(loading:Boolean) {
        when(loading) {
            true -> {
                isScrolling = true
                progress_movie_next_page.visibility = View.VISIBLE
            }
            false -> {
                isScrolling = false
                progress_movie_next_page.visibility = View.GONE
            }
        }
    }

    private fun showMovie(response:MovieResponse) {
        totalPage = response.total_pages!!.toInt()
        mainAdapter.setData(response.results)
    }

    private fun showMovieNextPage(response:MovieResponse) {
        totalPage = response.total_pages!!.toInt()
        mainAdapter.setDataNextPage(response.results)
        showMessage("Page $currentPage")
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_popular -> {
                showMessage("Movie Popular selected")
                movieCategory = moviePopular
                getMovie()
                true
            }
            R.id.action_now_playing -> {
                showMessage("Movie Now Playing selected")
                movieCategory = movieNowPlaying
                getMovie()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}