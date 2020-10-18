package com.example.moviereviewandtrailer.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviereviewandtrailer.R
import com.example.moviereviewandtrailer.activity.DetailActivity
import com.example.moviereviewandtrailer.adapter.MainAdapter
import com.example.moviereviewandtrailer.moviemodel.Constant
import com.example.moviereviewandtrailer.moviemodel.MovieModel
import com.example.moviereviewandtrailer.moviemodel.MovieResponse
import com.example.moviereviewandtrailer.retrofit.ApiServices
import kotlinx.android.synthetic.main.fragment_popular.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularFragment : Fragment() {
    lateinit var v:View
    lateinit var mainAdapter: MainAdapter
    private var isScrolling = false
    private var currentPage = 1
    private var totalPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_popular, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupReyclerView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getMoviePopular()
        showLoadingNextPage(false)
    }

    private fun setupReyclerView() {
        mainAdapter = MainAdapter(arrayListOf(), object : MainAdapter.OnAdapterListener{
            override fun OnClick(movie: MovieModel) {
                startActivity(Intent(requireContext(), DetailActivity::class.java))
            }
        })
        v.list_movie.apply {
            layoutManager = GridLayoutManager(context,2)
            adapter = mainAdapter
        }
    }

    private fun setupListener() {
        v.scroll_view.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
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
                            getMoviePopularNextPage()
                        }
                    }
                }
            }

        })
    }

    private fun getMoviePopular() {
        v.scroll_view.scrollTo(0,0)
        currentPage = 1
        showLoading(true)

        ApiServices()
            .end_point
            .getMoviePopular(Constant.api_key,currentPage)
            .enqueue(object: Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoading((false))
                    if(response.isSuccessful) {
                        showMovie(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
//                    Log.d(TAG, "errorResponse: $t")
                    showLoading((false))
                }

            })
    }

    private fun getMoviePopularNextPage() {
        currentPage += 1
        showLoadingNextPage(true)

        ApiServices()
            .end_point
            .getMoviePopular(Constant.api_key,currentPage)
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
//                    Log.d(TAG, "errorResponse: $t")
                    showLoadingNextPage(false)
                }

            })
    }

    private fun showLoading(loading:Boolean) {
        when(loading) {
            true -> v.progress_movie.visibility = View.VISIBLE
            false -> v.progress_movie.visibility = View.GONE
        }
    }

    private fun showLoadingNextPage(loading:Boolean) {
        when(loading) {
            true -> {
                isScrolling = true
                v.progress_movie_next_page.visibility = View.VISIBLE
            }
            false -> {
                isScrolling = false
                v.progress_movie_next_page.visibility = View.GONE
            }
        }
    }

    private fun showMovie(response: MovieResponse) {
        totalPage = response.total_pages!!.toInt()
        mainAdapter.setData(response.results)
    }

    private fun showMovieNextPage(response: MovieResponse) {
        totalPage = response.total_pages!!.toInt()
        mainAdapter.setDataNextPage(response.results)
        Toast.makeText(requireContext(),"Page $currentPage", Toast.LENGTH_SHORT).show()
    }
}