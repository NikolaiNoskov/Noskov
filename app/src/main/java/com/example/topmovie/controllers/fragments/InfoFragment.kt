package com.example.topmovie.controllers.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.topmovie.R
import com.example.topmovie.data.entity.byId_req.Film

import com.example.topmovie.databinding.FragmentInfoBinding
import com.example.topmovie.retrofit.Common
import com.example.topmovie.retrofit.RetrofitService
import com.example.topmovie.utils.NetWorkHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoFragment:Fragment(R.layout.fragment_info) {
    private var binding : FragmentInfoBinding?= null

    private val option = RequestOptions
        .diskCacheStrategyOf(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentInfoBinding.bind(view)

        val filmID = arguments?.getInt(ARG_NAME)
        if(NetWorkHelper.internetIsAvailable(requireContext())) {
            sendRequest(filmID.toString())
        } else {
            binding?.noConnectionView?.group?.visibility = View.VISIBLE
        }

        binding?.run {
            noConnectionView.group.setOnClickListener {
                if (NetWorkHelper.internetIsAvailable(requireContext())) {
                    binding?.noConnectionView?.group?.visibility = View.GONE
                    sendRequest(filmID.toString())
                }
            }
            noConnectionView.btnRetry.setOnClickListener {
                if (NetWorkHelper.internetIsAvailable(requireContext())) {
                    binding?.noConnectionView?.group?.visibility = View.GONE
                    sendRequest(filmID.toString())
                }
            }
        }
    }
    private fun sendRequest(id: String) {
        val mService : RetrofitService =  Common.retrofitService(activity?.applicationContext)
        mService.getFilmById(id).enqueue(object : Callback<Film> {

            override fun onFailure(call: Call<Film>, t: Throwable) {
                binding?.noConnectionView?.group?.visibility = View.VISIBLE
            }

            override fun onResponse(
                call: Call<Film>,
                response: Response<Film>
            ) {
                if(response.body() == null) {

                } else {
                    val glide = Glide.with(this@InfoFragment)
                    val film: Film = response.body() as Film


                    binding?.run {
                        glide
                            .load(film.posterUrl)
                            .apply(option)
                            .placeholder(R.drawable.ic_baseline_movie_creation_24)
                            .error(R.drawable.ic_baseline_movie_creation_24)
                            .into(ivPoster)
                        tvTitle.text = film.nameRu
                        tvContent.text = film.description
                        tvCountries.text = "Страны: " + film.countries.get(0).country
                        tvGenres.text = "Жанры: " + film.genres.get(0).genre

                    }

                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        private const val ARG_NAME = "id"
        fun newInstance(id: Int) = InfoFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_NAME, id)
            }
        }
    }

}
