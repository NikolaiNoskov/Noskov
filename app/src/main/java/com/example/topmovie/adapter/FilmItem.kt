package com.example.topmovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import com.example.topmovie.R
import com.example.topmovie.data.entity.top_req.FilmFromTop
import com.example.topmovie.databinding.ItemFilmBinding

class FilmItem(
    private val binding: ItemFilmBinding,
    private val glide: RequestManager,
    private val action: (FilmFromTop) -> Unit,
    private val longClickAction: (FilmFromTop) -> Unit,
) :  RecyclerView.ViewHolder(binding.root) {

    private val option = RequestOptions
        .diskCacheStrategyOf(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)

    fun onBind(filmFromTop: FilmFromTop) {
        with(binding) {
            if(filmFromTop.favourite==true) {
                ivStar.visibility = View.VISIBLE
            } else {
                ivStar.visibility = View.INVISIBLE
            }
            tvTitle.text = filmFromTop.nameRu
            tvDescription.text = filmFromTop.genres.get(0).genre +" (" + filmFromTop.year +")"
            glide
                .load(filmFromTop.posterUrlPreview)
                .apply(option)
                .placeholder(R.drawable.ic_baseline_movie_creation_24)
                .error(R.drawable.ic_baseline_movie_creation_24)
                .into(ivPoster)

            root.setOnClickListener {
                action(filmFromTop)
            }
            root.setOnLongClickListener {
                longClickAction(filmFromTop)
                true
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup,
                   glide: RequestManager,
                   action: (FilmFromTop) -> Unit,
                   longClickAction: (FilmFromTop) -> Unit,
                   ) : FilmItem =
            FilmItem(
                binding = ItemFilmBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                glide = glide,
                action = action,
                longClickAction = longClickAction
            )
    }
}

