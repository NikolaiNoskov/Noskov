package com.example.topmovie.controllers.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
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
import com.example.topmovie.data.entity.top_req.FilmFromTopResponse
import com.example.topmovie.databinding.FragmentMainBinding
import com.example.topmovie.retrofit.Common
import com.example.topmovie.retrofit.RetrofitService
import com.example.topmovie.utils.NavigationManager.navigateTo
import com.example.topmovie.utils.NavigationManager.navigateWithExtrasTo
import com.example.topmovie.utils.NetWorkHelper.internetIsAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainFragment : Fragment(R.layout.fragment_main)  {

    private var binding : FragmentMainBinding?= null
    private var listAdapter : FilmListAdapter ?= null
    private var films : List<FilmFromTop>?=null
    private var favouritesIds : List<Int> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding = FragmentMainBinding.bind(view)
        updateListWithFavouriteFilm()


        if(internetIsAvailable(requireContext())) {
            startShowLoading()
            sendRequest()
        } else {
            binding?.noConnectionView?.group?.visibility = View.VISIBLE
        }

        binding?.run {
            etFilter.addTextChangedListener(
                afterTextChanged = { filter( it.toString() )
                    },)




            noConnectionView.btnRetry.setOnClickListener {
                if (internetIsAvailable(requireContext())) {
                    binding?.noConnectionView?.group?.visibility = View.GONE
                    sendRequest()
                }
            }

            btnFavourite.setOnClickListener {
                navigateTo(
                    requireContext(),
                    parentFragmentManager,
                    FavouriteFragments(),
                    true
                )
            }

        }
    }

    private fun sendRequest() {
        val mService : RetrofitService = Common.retrofitService(activity?.applicationContext)
        mService.getTop100Films().enqueue(object : Callback<FilmFromTopResponse> {

            override fun onFailure(call: Call<FilmFromTopResponse>, t: Throwable) {
                binding?.noConnectionView?.group?.visibility = View.VISIBLE
            }

            override fun onResponse(
                call: Call<FilmFromTopResponse>,
                response: Response<FilmFromTopResponse>
            ) {
                if(response.body() == null) {

                } else {
                    val filmFromTopResponse: FilmFromTopResponse = response.body() as FilmFromTopResponse
                    films = filmFromTopResponse.films

                    films?.filter{ it.filmId in favouritesIds }?.forEach { it.favourite = true }

                    val itemDecoration = context?.let {
                        SpaceItemDecorator(
                            it,
                            18.0f
                        )
                    }

                    binding?.run {
                        listAdapter = FilmListAdapter(Glide.with(this@MainFragment),
                            { navigateWithExtrasTo(requireContext(), parentFragmentManager, it.filmId, false) },
                            { saveInDB(it) }
                        )

                        listAdapter?.submitList(films)
                        rvFilms.adapter = listAdapter
                        rvFilms.layoutManager = LinearLayoutManager(context)
                        rvFilms.addItemDecoration(itemDecoration!!)
                    }
                    stopShowLoading()
                }
            }
        })
    }


    private fun saveInDB(film: FilmFromTop) {
        film.favourite = true
        film.genresDB = film.genres.get(0).genre
        val repository = FilmRepository(requireContext())
        lifecycleScope.launch(context = Dispatchers.IO) {
            repository.saveFilm(film)
        }
        listAdapter?.notifyItemChanged(films!!.indexOf(film))
    }


    private fun filter(filterParam: String) {
        val sortedList= mutableListOf<FilmFromTop>()
        films?.let {   for (film in it ){
            if(film.nameRu?.lowercase()?.contains(filterParam.lowercase()) == true)
                sortedList.add(film)
        }
            listAdapter?.submitList(sortedList)
        }
    }


    private fun updateListWithFavouriteFilm() {
        val repository = FilmRepository(requireContext())
        lifecycleScope.launch(Dispatchers.Main) {
            val res = async(Dispatchers.IO) {
                val ans = repository.getAllFilms()
                ans.forEach {
                    it.genres.add(Genre(it.genresDB))
                }
                val favouriteIds = mutableListOf<Int>()
                for(i in ans){
                    favouriteIds.add(i.filmId)
                }
                favouriteIds
            }
            favouritesIds = res.await()
            listAdapter?.apply{
                films?.filter {it.filmId in favouritesIds}?.forEach {
                    it.favourite = true
                    this.notifyItemChanged(films!!.indexOf(it))
                }
            }
        }
    }


    private fun startShowLoading(){
        binding?.pbLoading?.visibility = View.VISIBLE
    }

    private fun stopShowLoading(){
        binding?.pbLoading?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        listAdapter = null
    }
}