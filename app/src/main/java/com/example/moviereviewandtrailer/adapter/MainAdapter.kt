package com.example.moviereviewandtrailer.adapter

import android.graphics.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereviewandtrailer.R
import com.example.moviereviewandtrailer.moviemodel.Constant
import com.example.moviereviewandtrailer.moviemodel.MovieModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_main.view.*

class MainAdapter(var movies: ArrayList<MovieModel>, var listener: OnAdapterListener):RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_main, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        val poster_path = Constant.POSTER_PATH + movie.poster_path
        holder.bind(movie)
        Picasso.get().load(poster_path)
            .placeholder(R.drawable.placeholder_portrait)
            .error(R.drawable.placeholder_portrait)
            .into(holder.view.image_poster);
        holder.view.image_poster.setOnClickListener {
            Constant.MOVIE_ID = movie.id!!
            Constant.MOVIE_TITLE = movie.title!!
            listener.OnClick(movie)
        }
    }

    override fun getItemCount() = movies.size

    class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val view = view
        fun bind(movies:MovieModel) {
            view.text_title.text = movies.title
        }
    }

    public fun setData(newMovies:List<MovieModel>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    public fun setDataNextPage(newMovies:List<MovieModel>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun OnClick(movie:MovieModel)
    }
}