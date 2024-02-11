package com.example.topmovie.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.example.topmovie.data.entity.top_req.FilmFromTop

class FilmListAdapter(
    private val glide: RequestManager,
    private val action: (FilmFromTop) -> Unit,
    private val longClickAction: (FilmFromTop) -> Unit,
    ): ListAdapter<FilmFromTop, FilmItem>(
    object : DiffUtil.ItemCallback<FilmFromTop>() {
    override fun areItemsTheSame(
        oldItem: FilmFromTop,
        newItem: FilmFromTop
    ): Boolean {
        return oldItem.filmId == newItem.filmId
    }

    override fun areContentsTheSame(
        oldItem: FilmFromTop,
        newItem: FilmFromTop
    ): Boolean {
        return oldItem.nameRu == newItem.nameRu
                && oldItem.posterUrlPreview==newItem.posterUrlPreview
                && oldItem.year == newItem.year
                && oldItem.favourite == newItem.favourite
    }
}

) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilmItem {
        return FilmItem.create(parent, glide, action,  longClickAction)

    }




    override fun onBindViewHolder(
        holder: FilmItem,
        position: Int
    ) {
            holder.onBind(getItem(position))

    }

}