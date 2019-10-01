package com.demo.cl.movieguidekotlin.ui

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.cl.movieguidekotlin.R
import com.demo.cl.movieguidekotlin.ViewModelFactory
import com.demo.cl.movieguidekotlin.api.Category
import com.demo.cl.movieguidekotlin.binding.OnSwipeRefreshListener
import com.demo.cl.movieguidekotlin.data.MovieDatabase
import com.demo.cl.movieguidekotlin.databinding.FramgentMoviesBinding
import com.demo.cl.movieguidekotlin.list.adapter.MoviePageListAdapter
import com.demo.cl.movieguidekotlin.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.framgent_movies.*

class MovieFragment : Fragment() {
    lateinit var viewmodel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FramgentMoviesBinding>(
            inflater,
            R.layout.framgent_movies,
            container,
            false
        ).apply {
            activity?.let {
                viewmodel = ViewModelProviders.of(it, ViewModelFactory.getInstance(it.applicationContext))
                    .get(MovieViewModel::class.java)
            }
            swipRefresh = object : OnSwipeRefreshListener {
                override fun swipeToRefresh(view: View) {
//                    viewmodel.getMovieList(viewmodel.currentCategory.value!!)
                }
            }
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_movies.apply {
            adapter = MoviePageListAdapter(viewmodel).apply {
                viewmodel.moviePageList.observe(this@MovieFragment, Observer {
                    submitList(it)
                })
            }
            layoutManager = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == (adapter?.itemCount ?: 0) - 1) {
                            2
                        } else {
                            1
                        }
                    }
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        viewmodel.getMovieList(Category.POPULAR)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_popular -> {
                viewmodel.getMovieList(Category.POPULAR)
                true
            }
            R.id.menu_top_rated -> {
                viewmodel.getMovieList(Category.TOP_RATED)
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }


    companion object {
        fun newInstance(): MovieFragment {
            return MovieFragment()
        }
    }
}