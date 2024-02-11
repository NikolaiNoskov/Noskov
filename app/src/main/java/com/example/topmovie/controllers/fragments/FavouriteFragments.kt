package com.example.topmovie.controllers.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.topmovie.R
import com.example.topmovie.adapter.FilmListAdapter
import com.example.topmovie.adapter.SpaceItemDecorator
import com.example.topmovie.data.db.repository.FilmRepository
import com.example.topmovie.data.entity.support.Genre
import com.example.topmovie.data.entity.top_req.FilmFromTop
import com.example.topmovie.databinding.FragmentFavouriteBinding
import com.example.topmovie.utils.NavigationManager.navigateTo
import com.example.topmovie.utils.NavigationManager.navigateWithExtrasTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavouriteFragments : Fragment(R.layout.fragment_favourite) {

    private var binding : FragmentFavouriteBinding?= null
    private var listAdapter : FilmListAdapter?= null
    private var films : List<FilmFromTop>?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavouriteBinding.bind(view)
        startShowLoading()
        binding?.etFilter?.addTextChangedListener(afterTextChanged = {filter(it.toString())})

        binding?.btnPopular?.setOnClickListener {
            navigateTo(requireContext(), parentFragmentManager, MainFragment(), true)
        }

        setRecyclerFromDB()
    }

    private fun filter(filterParam: String) {
        val sortedList= mutableListOf<FilmFromTop>()
        films?.let {
            for (film in it) {
                if (film.nameRu?.lowercase()?.contains(filterParam.lowercase()) == true)
                    sortedList.add(film)
            }
            listAdapter?.submitList(sortedList)
        }
    }

    private fun setRecyclerFromDB(){
        val repository = FilmRepository(requireContext())
        lifecycleScope.launch(Dispatchers.Main) {
            val res =  async(Dispatchers.IO) {
                val filmsFromBD = repository.getAllFilms()
                filmsFromBD.forEach {
                    it.genres.add(
                        Genre( it.genresDB)
                    )}
                films = filmsFromBD
                filmsFromBD
            }

            val itemDecoration = context?.let {
                SpaceItemDecorator(
                    it,
                    18.0f
                )
            }

            binding?.run {
                listAdapter = FilmListAdapter(glide = Glide.with(this@FavouriteFragments),
                    { navigateWithExtrasTo (requireContext(), parentFragmentManager,  it.filmId, false) },
                    {  }
                )
                listAdapter?.submitList(res.await())
                rvFilms.adapter = listAdapter
                rvFilms.layoutManager = LinearLayoutManager(context)
                rvFilms.addItemDecoration(itemDecoration!!)
            }
            stopShowLoading()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        listAdapter = null
    }

    private fun startShowLoading(){
        binding?.pbLoading?.visibility = View.VISIBLE
    }

    private fun stopShowLoading(){
        binding?.pbLoading?.visibility = View.GONE
    }
}