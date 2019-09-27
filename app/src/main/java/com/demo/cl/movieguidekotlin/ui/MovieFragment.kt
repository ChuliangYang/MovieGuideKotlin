package com.demo.cl.movieguidekotlin.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.cl.movieguidekotlin.R
import com.demo.cl.movieguidekotlin.ViewModelFactory
import com.demo.cl.movieguidekotlin.api.Category
import com.demo.cl.movieguidekotlin.list.adapter.MoviePageListAdapter
import com.demo.cl.movieguidekotlin.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.framgent_movies.*

class MovieFragment : Fragment() {
    lateinit var viewmodel:MovieViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.framgent_movies,container,false).apply {
            activity?.let {
                viewmodel= ViewModelProviders.of(it,ViewModelFactory.getInstance()).get(MovieViewModel::class.java)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_movies.apply {
            layoutManager=GridLayoutManager(activity,2,RecyclerView.VERTICAL,false)
            adapter=MoviePageListAdapter().apply {
                viewmodel.moviePageList.observe(this@MovieFragment, Observer {
                    submitList(it)
                })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewmodel.getMovieList(Category.POPULAR)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }


    companion object {
        fun newInstance(): MovieFragment {
            return MovieFragment()
        }
    }
}