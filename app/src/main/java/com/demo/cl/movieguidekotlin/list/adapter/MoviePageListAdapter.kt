package com.demo.cl.movieguidekotlin.list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.demo.cl.movieguidekotlin.BR
import com.demo.cl.movieguidekotlin.R
import com.demo.cl.movieguidekotlin.data.Movie
import com.demo.cl.movieguidekotlin.databinding.ItemMovieBinding
import com.demo.cl.movieguidekotlin.databinding.ItemProgressBarBinding
import com.demo.cl.movieguidekotlin.viewmodel.MovieViewModel

class MoviePageListAdapter(val viewModel:MovieViewModel):PagedListAdapter<Movie,ViewBindingHolder>(object : DiffUtil.ItemCallback<Movie?>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return true
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingHolder {
        return if(viewType == ItemViewType.MovieItem.value){
            ViewBindingHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_movie,parent,false))
        }else{
            ViewBindingHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_progress_bar,parent,false))
        }
    }

    override fun onBindViewHolder(holder: ViewBindingHolder, position: Int) {
        Log.e("bind","${position}")
        if(holder.dataBinding is ItemProgressBarBinding){
            holder.setData(BR.viewModel,viewModel)
//            holder.dataBinding.lifecycleOwner=holder.itemView.context as LifecycleOwner
        }else if(holder.dataBinding is ItemMovieBinding){
            holder.setData(BR.movie,getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position > (currentList?.size?:0) -1 ){
            ItemViewType.BottomView.value
        }else{
            ItemViewType.MovieItem.value
        }
    }

    override fun getItemCount(): Int {
//        return (currentList?.size?:0) + 1
        return (currentList?.size?:0)
    }
}

class ViewBindingHolder(val dataBinding: ViewDataBinding):RecyclerView.ViewHolder(dataBinding.root){
    fun <T> setData(id:Int,data:T){
        dataBinding.setVariable(id,data)
        dataBinding.executePendingBindings()
    }
}

enum class ItemViewType(val value:Int){
    BottomView(0),
    MovieItem(1)
}